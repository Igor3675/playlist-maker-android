package ru.yandex.practicum.playlistmaker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import ru.yandex.practicum.playlistmaker.ui.search.SearchScreen
import ru.yandex.practicum.playlistmaker.ui.theme.PlaylistMakerTheme

class SearchActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlaylistMakerTheme {
                SearchScreen(onBackClick = { finish() })
            }
        }
    }
}
