package ru.yandex.practicum.playlistmaker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = YpBlue,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = YpBlack,
    surface = YpBlack,
    onPrimary = YpWhite,
    onBackground = YpWhite,
    onSurface = YpWhite
)

private val LightColorScheme = lightColorScheme(
    primary = YpBlue,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    background = YpWhite,
    surface = YpWhite,
    onPrimary = YpWhite,
    onBackground = YpBlack,
    onSurface = YpBlack
)

@Composable
fun PlaylistMakerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
