package ru.yandex.practicum.playlistmaker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.yandex.practicum.playlistmaker.ui.main.MainScreen
import ru.yandex.practicum.playlistmaker.ui.search.SearchScreen
import ru.yandex.practicum.playlistmaker.ui.setting.SettingsScreen

@Composable
fun PlaylistHost(
    navController: NavHostController = rememberNavController()
) {
    fun navigateToSearch() {
        navController.navigate(Screen.SEARCH.route)
    }

    fun navigateToSettings() {
        navController.navigate(Screen.SETTINGS.route)
    }

    fun navigateBack() {
        navController.popBackStack()
    }

    NavHost(
        navController = navController,
        startDestination = Screen.MAIN.route
    ) {
        composable(Screen.MAIN.route) {
            MainScreen(
                onSearchClick = ::navigateToSearch,
                onSettingsClick = ::navigateToSettings
            )
        }
        composable(Screen.SEARCH.route) {
            SearchScreen(
                onBackClick = ::navigateBack
            )
        }
        composable(Screen.SETTINGS.route) {
            SettingsScreen(
                onBackClick = ::navigateBack
            )
        }
    }
}
