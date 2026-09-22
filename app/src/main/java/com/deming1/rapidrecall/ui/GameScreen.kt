package com.deming1.rapidrecall.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material3.Text
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.deming1.rapidrecall.R
import androidx.compose.ui.tooling.preview.Preview
import com.deming1.rapidrecall.ui.theme.RapidRecallTheme
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import com.deming1.rapidrecall.GameViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.Button

/*
* The GameScreen function handles all the UI components in the game screen, such as the buffer texts,
* the flashing sequences and final results with color indicators.
* */
@Composable
fun GameScreen(
    gameViewModel: GameViewModel,
    onUserInputChange: (String) -> Unit,
    correctDigits: Int,
    currentDigits: Int,
    correctRecall: Boolean,
    wrongRecall: Boolean,
    onSubmit: () -> Unit,
    onReturn: () -> Unit,
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
            text = gameViewModel.currentText,
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
                    Text(
                        text = stringResource(R.string.correct_recall) + " $correctDigits/$currentDigits",
                        fontSize = 20.sp
                    )
                } else if (wrongRecall) {
                    Text(
                        text = stringResource(R.string.wrong_recall) + " $correctDigits/$currentDigits",
                        fontSize = 20.sp
                    )
                } else {
                    Text(
                        text = stringResource(R.string.enter_your_word),
                        fontSize = 20.sp
                    )
                }
            },
            enabled = gameViewModel.allowInput,
            isError = wrongRecall,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { onSubmit() }
            )
        )
        Spacer(modifier = modifier.height(25.dp))
        Button(
            onClick = {
                if (correctRecall || wrongRecall) {
                    onReturn()
                } else {
                    onSubmit()
                }
            },
            modifier = modifier
        ) {
            Text(
                text = if (correctRecall || wrongRecall) {
                    stringResource(R.string.Return)
                } else {
                    stringResource(R.string.submit)
                },
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
            onUserInputChange = {},
            correctDigits = 10,
            currentDigits = 10,
            correctRecall = true,
            wrongRecall = false,
            onSubmit = {},
            onReturn = {}
        )
    }
}