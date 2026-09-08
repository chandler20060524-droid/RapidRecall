package com.deming1.rapidrecall

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.annotation.StringRes
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.composable
import com.deming1.rapidrecall.ui.GameScreen
import com.deming1.rapidrecall.ui.StartScreen

enum class RapidRecallScreens(@StringRes val title: Int) {
    Start(title = R.string.start),
    Game(title = R.string.game),
    Summary(title = R.string.summary)
}

@Composable
fun RapidRecallApp(
    gameViewModel: GameViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    val gameUiState by gameViewModel.uiState.collectAsStateWithLifecycle()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = RapidRecallScreens.valueOf(
        backStackEntry?.destination?.route ?: RapidRecallScreens.Start.name
    )

    NavHost(
        navController = navController,
        startDestination = RapidRecallScreens.Start.name
    ) {
        composable(route = RapidRecallScreens.Start.name) {
            StartScreen(
                onStartButtonClicked = {
                    gameViewModel.generateSequence(it)
                    navController.navigate(RapidRecallScreens.Game.name)
                }
            )
        }

        composable(route = RapidRecallScreens.Game.name) {
            LaunchedEffect(Unit) {
                gameViewModel.startGame()
            }
            val currentStep by gameViewModel.gameStepState.collectAsStateWithLifecycle()
            var currentText by rememberSaveable { mutableStateOf("") }
            currentText = when (currentStep) {
                is GameStep.PreGameStep -> stringResource((currentStep as GameStep.PreGameStep).stringId)
                is GameStep.FlashSequenceStep -> (currentStep as GameStep.FlashSequenceStep).seqText
            }

            val allowInput = when (currentStep) {
                GameStep.PreGameStep(R.string.do_you_recall) -> true
                else -> false
            }

            GameScreen(
                gameViewModel = gameViewModel,
                sequence = gameUiState.currentSequence,
                seqLen = gameUiState.currentDigits,
                currentText = currentText,
                allowInput = allowInput,
                onUserInputChange = { userInput ->
                    if (userInput.length <= gameUiState.currentDigits && userInput.all { it.isDigit() }) {
                        gameViewModel.updateUserInput(userInput)
                    }
                },
                correctRecall = gameUiState.correct,
                wrongRecall = gameUiState.wrong,
                onKeyboardDone = {
                    gameViewModel.checkUserInput()
                    allowInput = false
                                 },
            )
        }

        composable(route = RapidRecallScreens.Summary.name) {

        }
    }
}