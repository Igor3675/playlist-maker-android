package ru.yandex.practicum.playlistmaker.ui.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ru.yandex.practicum.playlistmaker.R
import ru.yandex.practicum.playlistmaker.domain.model.Track
import ru.yandex.practicum.playlistmaker.ui.search.TrackListItem
import ru.yandex.practicum.playlistmaker.ui.view_model.PlaylistsViewModel

@Composable
fun FavoritesScreen(
    playlistsViewModel: PlaylistsViewModel,
    onTrackClick: (Track) -> Unit,
    onBackClick: () -> Unit
) {
    val favoriteTracks by playlistsViewModel.favoriteList.collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = dimensionResource(id = R.dimen.padding_small),
                    vertical = dimensionResource(id = R.dimen.padding_medium)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    contentDescription = stringResource(R.string.back),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.padding_regular)))
            Text(
                text = stringResource(R.string.favorites),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium
            )
        }

        if (favoriteTracks.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.favorites_empty),
                    fontSize = 18.sp,
                    color = colorResource(id = R.color.yp_text_gray)
                )
            }
        } else {
            val dividerThickness = dimensionResource(id = R.dimen.divider_thickness)
            val lightGray = colorResource(id = R.color.yp_light_gray)
            val standardPadding = dimensionResource(id = R.dimen.padding_standard)

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(favoriteTracks) { track ->
                    TrackListItem(
                        track = track,
                        onClick = { onTrackClick(track) }
                    )
                    HorizontalDivider(
                        thickness = dividerThickness,
                        color = lightGray,
                        modifier = Modifier.padding(horizontal = standardPadding)
                    )
                }
            }
        }
    }
}
