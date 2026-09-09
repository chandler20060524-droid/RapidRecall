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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.composable
import com.deming1.rapidrecall.ui.GameScreen
import com.deming1.rapidrecall.ui.StartScreen
import androidx.compose.ui.text.AnnotatedString
import com.deming1.rapidrecall.ui.SummaryScreen

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
            val currentStep by gameViewModel.gameStepState.collectAsStateWithLifecycle(TextStep.StringIdStep(R.string.greet))

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
                    (gameUiState.totalCorrectDigits / gameUiState.totalDigits).toFloat()
                },
                previousAttempts = gameUiState.previousAttempts
            )
        }
    }
}