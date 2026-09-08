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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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

            GameScreen(
                gameViewModel = gameViewModel,
                sequence = gameUiState.currentSequence,
                seqLen = gameUiState.currentDigits,
                onUserInputChange = { gameViewModel.updateUserInput(it) },
                correctRecall = gameUiState.correct,
                wrongRecall = gameUiState.wrong,
                onKeyboardDone = { gameViewModel.checkUserInput() },
            )
        }

        composable(route = RapidRecallScreens.Summary.name) {

        }
    }
}