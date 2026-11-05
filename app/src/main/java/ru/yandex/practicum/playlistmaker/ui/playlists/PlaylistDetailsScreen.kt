package ru.yandex.practicum.playlistmaker.ui.playlists

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun PlaylistDetailsScreen(
    playlistId: Long,
    playlistsViewModel: PlaylistsViewModel,
    onTrackClick: (Track) -> Unit,
    onBackClick: () -> Unit
) {
    val playlist by playlistsViewModel.getPlaylist(playlistId).collectAsState(initial = null)
    val standardPadding = dimensionResource(id = R.dimen.padding_standard)
    val smallPadding = dimensionResource(id = R.dimen.padding_small)
    val lightGray = colorResource(id = R.color.yp_light_gray)
    val textGray = colorResource(id = R.color.yp_text_gray)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = smallPadding,
                    vertical = dimensionResource(id = R.dimen.padding_medium)
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    contentDescription = stringResource(R.string.back),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            if (playlist != null) {
                IconButton(
                    onClick = {
                        playlistsViewModel.removePlaylist(playlistId)
                        onBackClick()
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_delete),
                        contentDescription = stringResource(R.string.delete_playlist),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }

        val currentPlaylist = playlist
        if (currentPlaylist != null) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = standardPadding)
            ) {
                Box(
                    modifier = Modifier
                        .size(dimensionResource(id = R.dimen.playlist_cover_size))
                        .align(Alignment.CenterHorizontally)
                        .background(
                            lightGray,
                            RoundedCornerShape(dimensionResource(id = R.dimen.corner_radius_large))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_music),
                        contentDescription = currentPlaylist.name,
                        modifier = Modifier.size(dimensionResource(id = R.dimen.playlist_cover_icon_size))
                    )
                }

                Spacer(modifier = Modifier.height(standardPadding))

                Text(
                    text = currentPlaylist.name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                if (currentPlaylist.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(smallPadding))
                    Text(
                        text = currentPlaylist.description,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                Spacer(modifier = Modifier.height(smallPadding))
                Text(
                    text = stringResource(R.string.tracks_count, currentPlaylist.tracks.size),
                    fontSize = 14.sp,
                    color = textGray
                )
            }

            Spacer(modifier = Modifier.height(standardPadding))

            if (currentPlaylist.tracks.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.playlist_empty_tracks),
                        fontSize = 16.sp,
                        color = textGray
                    )
                }
            } else {
                val dividerThickness = dimensionResource(id = R.dimen.divider_thickness)
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(currentPlaylist.tracks) { track ->
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
}
