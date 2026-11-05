package ru.yandex.practicum.playlistmaker.ui.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ru.yandex.practicum.playlistmaker.domain.model.Track
import ru.yandex.practicum.playlistmaker.ui.favorites.FavoritesScreen
import ru.yandex.practicum.playlistmaker.ui.main.MainScreen
import ru.yandex.practicum.playlistmaker.ui.playlists.NewPlaylistScreen
import ru.yandex.practicum.playlistmaker.ui.playlists.PlaylistDetailsScreen
import ru.yandex.practicum.playlistmaker.ui.playlists.PlaylistsScreen
import ru.yandex.practicum.playlistmaker.ui.search.DetailsScreen
import ru.yandex.practicum.playlistmaker.ui.search.SearchScreen
import ru.yandex.practicum.playlistmaker.ui.setting.SettingsScreen
import ru.yandex.practicum.playlistmaker.ui.view_model.PlaylistsViewModel
import ru.yandex.practicum.playlistmaker.ui.view_model.SearchViewModel

@Composable
fun PlaylistHost(navController: NavHostController = rememberNavController()) {
    val searchViewModel: SearchViewModel = viewModel(
        factory = SearchViewModel.getViewModelFactory()
    )
    val playlistsViewModel: PlaylistsViewModel = viewModel(
        factory = PlaylistsViewModel.getViewModelFactory()
    )

    val navigateToDetails: (Track) -> Unit = { track ->
        val encodedTrack = Uri.encode(track.trackName)
        val encodedArtist = Uri.encode(track.artistName)
        val encodedTime = Uri.encode(track.trackTime)
        navController.navigate("details/$encodedTrack/$encodedArtist/$encodedTime")
    }

    NavHost(
        navController = navController,
        startDestination = Screen.MAIN.route
    ) {
        composable(Screen.MAIN.route) {
            MainScreen(
                onSearchClick = { navController.navigate(Screen.SEARCH.route) },
                onPlaylistsClick = { navController.navigate(Screen.PLAYLISTS.route) },
                onFavoritesClick = { navController.navigate(Screen.FAVORITES.route) },
                onSettingsClick = { navController.navigate(Screen.SETTINGS.route) }
            )
        }

        composable(Screen.SEARCH.route) {
            SearchScreen(
                viewModel = searchViewModel,
                onTrackClick = navigateToDetails,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.PLAYLISTS.route) {
            val playlists by playlistsViewModel.playlists.collectAsState(initial = emptyList())
            PlaylistsScreen(
                playlists = playlists,
                addNewPlaylist = { navController.navigate(Screen.CREATE_PLAYLIST.route) },
                navigateToPlaylist = { id -> navController.navigate("playlist/$id") },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.CREATE_PLAYLIST.route) {
            NewPlaylistScreen(
                onSavePlaylist = { name, description ->
                    playlistsViewModel.createNewPlayList(name, description)
                    navController.popBackStack()
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.PLAYLIST_DETAILS.route,
            arguments = listOf(navArgument("playlistId") { type = NavType.LongType })
        ) { backStackEntry ->
            val playlistId = backStackEntry.arguments?.getLong("playlistId") ?: 0L
            PlaylistDetailsScreen(
                playlistId = playlistId,
                playlistsViewModel = playlistsViewModel,
                onTrackClick = navigateToDetails,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.FAVORITES.route) {
            FavoritesScreen(
                playlistsViewModel = playlistsViewModel,
                onTrackClick = navigateToDetails,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.SETTINGS.route) {
            SettingsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.DETAILS.route,
            arguments = listOf(
                navArgument("trackName") { type = NavType.StringType },
                navArgument("artistName") { type = NavType.StringType },
                navArgument("trackTime") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val trackName = Uri.decode(backStackEntry.arguments?.getString("trackName").orEmpty())
            val artistName = Uri.decode(backStackEntry.arguments?.getString("artistName").orEmpty())
            val trackTime = Uri.decode(backStackEntry.arguments?.getString("trackTime").orEmpty())
            DetailsScreen(
                track = Track(trackName = trackName, artistName = artistName, trackTime = trackTime),
                playlistsViewModel = playlistsViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
