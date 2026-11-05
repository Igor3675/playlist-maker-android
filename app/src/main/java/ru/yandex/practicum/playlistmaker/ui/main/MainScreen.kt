package ru.yandex.practicum.playlistmaker.ui.main

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import ru.yandex.practicum.playlistmaker.R
import ru.yandex.practicum.playlistmaker.ui.theme.PlaylistMakerTheme

@Composable
fun MainScreen(
    onSearchClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onPlaylistsClick: () -> Unit = {},
    onFavoritesClick: () -> Unit = {}
) {
    val topCornerRadius = dimensionResource(id = R.dimen.corner_radius_large)
    val standardPadding = dimensionResource(id = R.dimen.padding_standard)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.yp_blue))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.toolbar_height))
                .padding(horizontal = standardPadding),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = stringResource(R.string.app_name),
                color = colorResource(id = R.color.yp_white),
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = topCornerRadius, topEnd = topCornerRadius)),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = dimensionResource(id = R.dimen.padding_medium),
                        start = standardPadding,
                        end = standardPadding
                    )
            ) {
                MainMenuItem(
                    iconRes = R.drawable.ic_search,
                    title = stringResource(R.string.search),
                    onClick = onSearchClick
                )
                MainMenuItem(
                    iconRes = R.drawable.ic_playlists,
                    title = stringResource(R.string.playlists),
                    onClick = onPlaylistsClick
                )
                MainMenuItem(
                    iconRes = R.drawable.ic_favorites,
                    title = stringResource(R.string.favorites),
                    onClick = onFavoritesClick
                )
                MainMenuItem(
                    iconRes = R.drawable.ic_settings,
                    title = stringResource(R.string.settings),
                    onClick = onSettingsClick
                )
            }
        }
    }
}

@Composable
fun MainMenuItem(
    @DrawableRes iconRes: Int,
    title: String,
    onClick: () -> Unit
) {
    val iconSize = dimensionResource(id = R.dimen.icon_size)
    val regularPadding = dimensionResource(id = R.dimen.padding_regular)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(dimensionResource(id = R.dimen.main_menu_item_height))
            .clickable(onClick = onClick)
            .padding(horizontal = regularPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = title,
                modifier = Modifier.size(iconSize),
                tint = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.width(regularPadding))
            Text(
                text = title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        Icon(
            painter = painterResource(id = R.drawable.ic_chevron_right),
            contentDescription = null,
            modifier = Modifier.size(iconSize),
            tint = colorResource(id = R.color.yp_text_gray)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    PlaylistMakerTheme {
        MainScreen(
            onSearchClick = {},
            onSettingsClick = {}
        )
    }
}
