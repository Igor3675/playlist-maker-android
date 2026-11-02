package ru.yandex.practicum.playlistmaker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.yandex.practicum.playlistmaker.ui.main.MainScreen
import ru.yandex.practicum.playlistmaker.ui.search.DetailsScreen
import ru.yandex.practicum.playlistmaker.ui.search.SearchScreen
import ru.yandex.practicum.playlistmaker.ui.setting.SettingsScreen
import ru.yandex.practicum.playlistmaker.ui.view_model.SearchViewModel
import ru.yandex.practicum.playlistmaker.ui.view_model.TrackViewModel

@Composable
fun PlaylistHost(
    navController: NavHostController = rememberNavController()
) {
    val searchViewModel: SearchViewModel = viewModel(factory = SearchViewModel.getViewModelFactory())
    val trackViewModel: TrackViewModel = viewModel()

    fun navigateToSearch() {
        navController.navigate(Screen.SEARCH.route)
    }

    fun navigateToSettings() {
        navController.navigate(Screen.SETTINGS.route)
    }

    fun navigateToDetails() {
        navController.navigate(Screen.DETAILS.route)
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
                viewModel = searchViewModel,
                onBackClick = ::navigateBack,
                onTrackClick = { track ->
                    trackViewModel.selectTrack(track)
                    navigateToDetails()
                }
            )
        }
        composable(Screen.SETTINGS.route) {
            SettingsScreen(
                onBackClick = ::navigateBack
            )
        }
        composable(Screen.DETAILS.route) {
            val selectedTrack by trackViewModel.selectedTrack.collectAsState()
            DetailsScreen(
                track = selectedTrack,
                onBackClick = ::navigateBack
            )
        }
    }
}
