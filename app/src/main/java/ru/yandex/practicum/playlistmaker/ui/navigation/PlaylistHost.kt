package ru.yandex.practicum.playlistmaker.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.yandex.practicum.playlistmaker.R
import ru.yandex.practicum.playlistmaker.ui.main.MainScreen
import ru.yandex.practicum.playlistmaker.ui.search.DetailsScreen
import ru.yandex.practicum.playlistmaker.ui.search.SearchScreen
import ru.yandex.practicum.playlistmaker.ui.setting.SettingsScreen
import ru.yandex.practicum.playlistmaker.ui.theme.YpTextGray
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

    fun navigateToPlaylists() {
        navController.navigate(Screen.PLAYLISTS.route)
    }

    fun navigateToFavorites() {
        navController.navigate(Screen.FAVORITES.route)
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
                onPlaylistsClick = ::navigateToPlaylists,
                onFavoritesClick = ::navigateToFavorites,
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
        composable(Screen.PLAYLISTS.route) {
            PlaceholderSectionScreen(
                title = stringResource(id = R.string.playlists),
                emptyMessage = stringResource(id = R.string.nothing_found),
                onBackClick = ::navigateBack
            )
        }
        composable(Screen.FAVORITES.route) {
            PlaceholderSectionScreen(
                title = stringResource(id = R.string.favorites),
                emptyMessage = stringResource(id = R.string.nothing_found),
                onBackClick = ::navigateBack
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

@Composable
private fun PlaceholderSectionScreen(
    title: String,
    emptyMessage: String,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_back),
                contentDescription = stringResource(id = R.string.back),
                modifier = Modifier
                    .size(24.dp)
                    .clickable(onClick = onBackClick),
                tint = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.width(24.dp))
            Text(
                text = title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = emptyMessage,
                color = YpTextGray,
                fontSize = 18.sp
            )
        }
    }
}
