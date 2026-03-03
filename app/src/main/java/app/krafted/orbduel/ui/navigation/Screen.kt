package app.krafted.orbduel.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import app.krafted.orbduel.game.GameMode
import app.krafted.orbduel.ui.HomeScreen
import app.krafted.orbduel.ui.ModeSelectScreen
import app.krafted.orbduel.ui.theme.DarkBg
import app.krafted.orbduel.viewmodel.BattleViewModel

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object ModeSelect : Screen("mode_select/{mode}") {
        fun createRoute(mode: String) = "mode_select/$mode"
    }

    object Battle : Screen("battle")
    object Reveal : Screen("reveal")
    object Result : Screen("result")
    object Leaderboard : Screen("leaderboard")
}

@Composable
fun OrbDuelNavHost(
    navController: NavHostController = rememberNavController(),
    battleViewModel: BattleViewModel = hiltViewModel()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onPlayVsAi = { navController.navigate(Screen.ModeSelect.createRoute("VS_AI")) },
                onPlayVsPlayer = { navController.navigate(Screen.ModeSelect.createRoute("VS_PLAYER")) },
                onLeaderboard = { navController.navigate(Screen.Leaderboard.route) }
            )
        }

        composable(
            route = Screen.ModeSelect.route,
            arguments = listOf(navArgument("mode") { type = NavType.StringType })
        ) { backStackEntry ->
            val mode = backStackEntry.arguments?.getString("mode") ?: "VS_AI"
            ModeSelectScreen(
                mode = mode,
                onDifficultySelected = { difficulty ->
                    battleViewModel.setGameMode(GameMode.VS_AI)
                    battleViewModel.setAiDifficulty(difficulty)
                    navController.navigate(Screen.Battle.route)
                },
                onStartPlayerGame = { player2Name ->
                    battleViewModel.setGameMode(GameMode.VS_PLAYER, player2Name)
                    navController.navigate(Screen.Battle.route)
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Battle.route) {
            Box(Modifier.fillMaxSize().background(DarkBg))
        }

        composable(Screen.Reveal.route) {
            Box(Modifier.fillMaxSize().background(DarkBg))
        }

        composable(Screen.Result.route) {
            Box(Modifier.fillMaxSize().background(DarkBg))
        }

        composable(Screen.Leaderboard.route) {
            Box(Modifier.fillMaxSize().background(DarkBg))
        }
    }
}
