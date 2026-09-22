package com.deming1.rapidrecall

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.annotation.StringRes
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.composable
import com.deming1.rapidrecall.ui.GameScreen
import com.deming1.rapidrecall.ui.StartScreen
import androidx.compose.ui.text.AnnotatedString
import com.deming1.rapidrecall.ui.SummaryScreen
import com.deming1.rapidrecall.ui.TextStep

/*
* Description:
* The RapidRecallScreens enum class stores three screens with their string ID as their unique titles
* respectively. They are passed into navController.navigate() method in order to navigate between
* screens.
*
* Design Rationale:
* I designed an enum class to store screen name because it is a standard method to manage screens,
* which helps prevent illegal names that crashes the app when passed into navigate() method.
*
* Outstanding Issue: None
 */
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
                },
                onSummaryIconClicked = {
                    navController.navigate(RapidRecallScreens.Summary.name)
                }
            )
        }

        composable(route = RapidRecallScreens.Game.name) {
            LaunchedEffect(Unit) {
                gameViewModel.startGame()
            }
            val currentStep by gameViewModel.gameStepState.collectAsStateWithLifecycle(TextStep.StringIdStep(R.string.remember_the_sequence))

            gameViewModel.updateCurrentText(
                when (currentStep) {
                    is TextStep.StringIdStep -> AnnotatedString(stringResource((currentStep as TextStep.StringIdStep).stringId))
                    is TextStep.StringStep -> (currentStep as TextStep.StringStep).seqText
                }
            )
            if (gameViewModel.currentText == AnnotatedString(stringResource(R.string.do_you_recall))) {
                gameViewModel.enableInput()
            } else {
                gameViewModel.disableInput()
            }

            GameScreen(
                gameViewModel = gameViewModel,
                onUserInputChange = { userInput ->
                    if (userInput.length <= gameUiState.currentDigits && userInput.all { it.isDigit() }) {
                        gameViewModel.updateUserInput(userInput)
                    }
                },
                correctDigits = gameUiState.currentCorrectDigits,
                currentDigits = gameUiState.currentDigits,
                correctRecall = gameUiState.correct,
                wrongRecall = gameUiState.wrong,
                onSubmit = { gameViewModel.checkUserInput() },
                onReturn = {
                    gameViewModel.resetCurrentText()
                    navController.navigate(RapidRecallScreens.Start.name)
                }
            )
        }

        composable(route = RapidRecallScreens.Summary.name) {
            SummaryScreen(
                percentage = if (gameUiState.totalDigits == 0) {
                        0.0f
                } else {
                    gameUiState.totalCorrectDigits / gameUiState.totalDigits
                },
                correctAttempt = gameUiState.correctAttempts,
                totalAttempt = gameUiState.totalAttempts,
                previousAttempts = gameUiState.previousAttempts,
                onBackButtonClicked = {
                    navController.navigate(RapidRecallScreens.Start.name)
                }
            )
        }
    }
}