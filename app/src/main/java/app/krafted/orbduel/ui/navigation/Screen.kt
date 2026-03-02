package app.krafted.orbduel.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import app.krafted.orbduel.ui.theme.DarkBg

sealed class Screen(val route: String) {
    object Home        : Screen("home")
    object ModeSelect  : Screen("mode_select/{mode}") {
        fun createRoute(mode: String) = "mode_select/$mode"
    }
    object Battle      : Screen("battle")
    object Reveal      : Screen("reveal")
    object Result      : Screen("result")
    object Leaderboard : Screen("leaderboard")
}

@Composable
fun OrbDuelNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController    = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            Box(Modifier.fillMaxSize().background(DarkBg))
        }

        composable(
            route     = Screen.ModeSelect.route,
            arguments = listOf(navArgument("mode") { type = NavType.StringType })
        ) {
            Box(Modifier.fillMaxSize().background(DarkBg))
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
