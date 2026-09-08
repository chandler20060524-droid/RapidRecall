package com.deming1.rapidrecall.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.runtime.ComposableOpenTarget
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.deming1.rapidrecall.R
import androidx.compose.ui.tooling.preview.Preview
import com.deming1.rapidrecall.ui.theme.RapidRecallTheme
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.produceState
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.deming1.rapidrecall.GameViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import android.content.Context
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalResources
import com.deming1.rapidrecall.GameStep
import androidx.compose.material3.Button

@Composable
fun GameScreen(
    gameViewModel: GameViewModel,
    sequence: String,
    seqLen: Int,
    currentText: String,
    allowInput: Boolean,
    onUserInputChange: (String) -> Unit,
    correctRecall: Boolean,
    wrongRecall: Boolean,
    onKeyboardDone: () -> Unit,
    modifier: Modifier = Modifier
) {
    val userInput = gameViewModel.userInput

    Column(
        modifier = modifier
            .fillMaxSize()
            .wrapContentSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = currentText,
            fontSize = 35.sp,
            modifier = modifier
        )
        Spacer(modifier = Modifier.height(50.dp))
        OutlinedTextField(
            value = userInput,
            singleLine = true,
            shape = shapes.large,
            onValueChange = onUserInputChange,
            label = {
                if (correctRecall) {
                    Text(stringResource(R.string.correct_recall))
                } else if (wrongRecall) {
                    Text(stringResource(R.string.wrong_recall))
                } else {
                    Text(stringResource(R.string.enter_your_word))
                }
            },
            enabled = allowInput,
            isError = wrongRecall,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { onKeyboardDone() }
            )
        )
        Spacer(modifier = modifier.height(25.dp))
        Button(
            onClick = { onKeyboardDone() },
            modifier = modifier
        ) {
            Text(
                text = stringResource(R.string.submit),
                fontSize = 20.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GameScreenPreview() {
    RapidRecallTheme() {
        GameScreen(
            gameViewModel = viewModel(),
            sequence = "0123456789",
            seqLen = 10,
            currentText = "Test",
            allowInput = false,
            onUserInputChange = {},
            correctRecall = false,
            wrongRecall = false,
            onKeyboardDone = {}
        )
    }
}