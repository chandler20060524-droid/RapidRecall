package com.deming1.rapidrecall

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.deming1.rapidrecall.ui.GameUiState
import kotlinx.coroutines.flow.update
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.launch

class GameViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()
    var userInput by mutableStateOf("")
        private set
    private val _gameStepState = MutableStateFlow<GameStep>(GameStep.PreGameStep(R.string.remember_the_sequence))
    val gameStepState: StateFlow<GameStep> = _gameStepState.asStateFlow()
    private val stringIdSequence = listOf(
        Pair(GameStep.PreGameStep(R.string.are_you_ready), 750.milliseconds),
        Pair(GameStep.PreGameStep(R.string.go), 750.milliseconds)
    )
    private val stringIdFlow: Flow<GameStep.PreGameStep> = flow {
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
                wrongIndices = mutableSetOf<Int>(),
                currentSequence = sb.toString(),
                currentDigits = seqLen,
                totalDigits = currentState.totalDigits + seqLen
            )
        }
    }

    fun updateUserInput(input: String) {
        userInput = input
    }

    fun checkUserInput() {
        val sequence = _uiState.value.currentSequence
        for (i in 0..<_uiState.value.currentDigits) {
            if (userInput[i] != sequence[i]) {
                _uiState.value.wrongIndices.add(i)
            }
        }
    }

    fun startGame() {
        viewModelScope.launch {
            generateSequenceFlow().collect { _gameStepState.value = it }
        }
    }

    private fun generateSequenceFlow(): Flow<GameStep> {
        val sequenceFlow = mutableListOf<String>()
        val sequence = _uiState.value.currentSequence
        val sb = StringBuilder()

        for (i in 0..<_uiState.value.currentDigits) {
            sb.clear()
            sb.append(" ".repeat(i))
            sb.append(sequence[i])
            sb.append(" ".repeat(_uiState.value.currentDigits - 1 - i))
            sequenceFlow.add(sb.toString())
        }

        val flashSequenceFlow: Flow<GameStep.FlashSequenceStep> = flow {
            sequenceFlow.forEach {
                emit(GameStep.FlashSequenceStep(it))
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

            emit(GameStep.PreGameStep(R.string.do_you_recall))
        }
    }
}