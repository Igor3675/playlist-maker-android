package ru.yandex.practicum.playlistmaker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import ru.yandex.practicum.playlistmaker.ui.setting.SettingsScreen
import ru.yandex.practicum.playlistmaker.ui.theme.PlaylistMakerTheme

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlaylistMakerTheme {
                SettingsScreen(onBackClick = { finish() })
            }
        }
    }
}
