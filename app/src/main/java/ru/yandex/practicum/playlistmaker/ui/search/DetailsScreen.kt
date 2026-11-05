package ru.yandex.practicum.playlistmaker.ui.search

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import ru.yandex.practicum.playlistmaker.R
import ru.yandex.practicum.playlistmaker.domain.model.Track
import ru.yandex.practicum.playlistmaker.ui.playlists.PlaylistListItem
import ru.yandex.practicum.playlistmaker.ui.view_model.PlaylistsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    track: Track,
    playlistsViewModel: PlaylistsViewModel? = null,
    onBackClick: () -> Unit
) {
    val currentTrackState = playlistsViewModel?.isExist(track)?.collectAsState(initial = track)
    val activeTrack = currentTrackState?.value ?: track
    var isFavorite by remember(activeTrack.favorite) { mutableStateOf(activeTrack.favorite) }
    var isBottomSheetOpen by remember { mutableStateOf(false) }
    val playlists by (playlistsViewModel?.playlists?.collectAsState(initial = emptyList())
        ?: remember { mutableStateOf(emptyList()) })
    val coroutineScope = rememberCoroutineScope()

    val lightGray = colorResource(id = R.color.yp_light_gray)
    val ypBlack = colorResource(id = R.color.yp_black)
    val ypRed = colorResource(id = R.color.yp_red)
    val textGray = colorResource(id = R.color.yp_text_gray)
    val mediumPadding = dimensionResource(id = R.dimen.padding_medium)
    val largePadding = dimensionResource(id = R.dimen.padding_large)
    val xlargePadding = dimensionResource(id = R.dimen.padding_xlarge)
    val actionButtonSize = dimensionResource(id = R.dimen.details_action_button_size)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(dimensionResource(id = R.dimen.padding_standard))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    contentDescription = stringResource(R.string.back),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Spacer(modifier = Modifier.width(mediumPadding))
            Text(
                text = stringResource(R.string.track_details),
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Spacer(modifier = Modifier.height(xlargePadding))

        Box(
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.details_cover_size))
                .align(Alignment.CenterHorizontally)
                .background(
                    lightGray,
                    RoundedCornerShape(dimensionResource(id = R.dimen.corner_radius_large))
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_music),
                contentDescription = activeTrack.trackName,
                modifier = Modifier.size(dimensionResource(id = R.dimen.details_cover_icon_size))
            )
        }

        Spacer(modifier = Modifier.height(largePadding))

        Text(
            text = activeTrack.trackName,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(mediumPadding))

        Text(
            text = "${stringResource(R.string.artist_label)}: ${activeTrack.artistName}",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(mediumPadding))

        Text(
            text = "${stringResource(R.string.duration_label)}: ${activeTrack.trackTime}",
            fontSize = 14.sp,
            color = textGray
        )

        Spacer(modifier = Modifier.height(xlargePadding))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { isBottomSheetOpen = true },
                modifier = Modifier
                    .size(actionButtonSize)
                    .background(lightGray, CircleShape)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add_playlist),
                    contentDescription = stringResource(R.string.add_to_playlist),
                    tint = ypBlack
                )
            }

            IconButton(
                onClick = {
                    val newFavorite = !isFavorite
                    isFavorite = newFavorite
                    coroutineScope.launch {
                        playlistsViewModel?.toggleFavorite(activeTrack, newFavorite)
                    }
                },
                modifier = Modifier
                    .size(actionButtonSize)
                    .background(lightGray, CircleShape)
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = stringResource(R.string.add_to_favorites),
                    tint = if (isFavorite) ypRed else ypBlack
                )
            }
        }
    }

    if (isBottomSheetOpen) {
        ModalBottomSheet(
            onDismissRequest = { isBottomSheetOpen = false },
            sheetState = rememberModalBottomSheetState()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = largePadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.add_to_playlist),
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.padding_regular))
                )
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(playlists, key = { it.id }) { playlist ->
                        PlaylistListItem(
                            playlist = playlist,
                            onClick = {
                                activeTrack.playlistId = playlist.id
                                coroutineScope.launch {
                                    playlistsViewModel?.insertSongToPlaylist(activeTrack, playlist.id)
                                    isBottomSheetOpen = false
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
