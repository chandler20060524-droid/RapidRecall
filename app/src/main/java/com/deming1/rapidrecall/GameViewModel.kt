package com.deming1.rapidrecall

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.deming1.rapidrecall.ui.GameUiState
import kotlinx.coroutines.flow.update
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.buildAnnotatedString
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.launch
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.graphics.Color
import com.deming1.rapidrecall.ui.TextStep

class GameViewModel: ViewModel() {
    private val TAG = "GameViewModel"

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()
    var userInput by mutableStateOf("")
        private set
    var allowInput by mutableStateOf(false)
        private set
    var currentText by mutableStateOf(AnnotatedString(""))
        private set
    private val _gameStepState = MutableStateFlow<TextStep>(TextStep.StringIdStep(R.string.remember_the_sequence))
    val gameStepState: StateFlow<TextStep> = _gameStepState.asStateFlow()
    private val stringIdSequence = listOf(
        Pair(TextStep.StringIdStep(R.string.are_you_ready), 750.milliseconds),
        Pair(TextStep.StringIdStep(R.string.go), 750.milliseconds)
    )
    private val stringIdFlow: Flow<TextStep.StringIdStep> = flow {
        delay(1000.milliseconds)
        for ((preGameStep, delayMillis) in stringIdSequence) {
            emit(preGameStep)
            delay(delayMillis)
        }
    }

    fun generateSequence(seqLen: Int) {
        val sb = StringBuilder()
        for (i in 1..seqLen) {
            sb.append((0..9).random())
        }

        userInput = ""
        _uiState.update { currentState ->
            currentState.copy(
                correct = false,
                wrong = false,
                currentSequence = sb.toString(),
                currentDigits = seqLen,
                totalDigits = currentState.totalDigits + seqLen
            )
        }
    }

    fun updateUserInput(input: String) {
        userInput = input
    }

    fun enableInput() {
        allowInput = true
    }

    fun disableInput() {
        allowInput = false
    }

    fun updateCurrentText(text: AnnotatedString) {
        currentText = text
    }

    fun checkUserInput() {
        var isCorrect = true
        val sequence = _uiState.value.currentSequence
        val seqLen = _uiState.value.currentDigits
        var correctDigits = 0
        for (i in 0..<seqLen) {
            if (userInput[i] != sequence[i]) {
                isCorrect = false
            } else {
                correctDigits++
            }
        }

        currentText = buildAnnotatedString {
            for (i in 0..<seqLen) {
                if (userInput[i] != sequence[i]) {
                    withStyle(style = SpanStyle(color = Color.Red)) {
                        append(sequence[i])
                    }
                } else {
                    withStyle(style = SpanStyle(color = Color.Green)) {
                        append(sequence[i])
                    }
                }
            }
        }

        _uiState.update { currentState ->
            currentState.copy(
                correct = isCorrect,
                wrong = !isCorrect,
                currentCorrectDigits = correctDigits,
                totalCorrectDigits = currentState.totalCorrectDigits + correctDigits,
                previousAttempts = currentState.previousAttempts + AttemptData(
                    userInput = userInput,
                    sequence = currentText,
                    correctDigits = correctDigits,
                    totalDigits = seqLen
                ),
                correctAttempts = currentState.correctAttempts + if (isCorrect) 1 else 0,
                totalAttempts = currentState.totalAttempts + 1
            )
        }
        _gameStepState.value = TextStep.StringStep(currentText)
    }

    fun startGame() {
        viewModelScope.launch {
            generateSequenceFlow().collect { _gameStepState.value = it }
        }
    }

    private fun generateSequenceFlow(): Flow<TextStep> {
        val sequenceFlow = mutableListOf<AnnotatedString>()
        val sequence = _uiState.value.currentSequence
        val seqLen = _uiState.value.currentDigits

        for (i in 0..<seqLen) {
            val str = buildAnnotatedString {
                append(" ".repeat(i))
                append(sequence[i])
                append(" ".repeat(seqLen - 1 - i))
            }
            sequenceFlow.add(str)
        }

        val flashSequenceFlow: Flow<TextStep.StringStep> = flow {
            sequenceFlow.forEach {
                emit(TextStep.StringStep(it))
                delay(250.milliseconds)
            }
        }

        return flow {
            stringIdFlow.collect { stringId ->
                emit(stringId)
            }

            flashSequenceFlow.collect { flashSequence ->
                emit(flashSequence)
            }

            emit(TextStep.StringIdStep(R.string.do_you_recall))
        }
    }

    fun resetCurrentText() {
        _gameStepState.value = TextStep.StringIdStep(R.string.remember_the_sequence)
    }
}