package ru.yandex.practicum.playlistmaker

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import ru.yandex.practicum.playlistmaker.ui.main.MainScreen
import ru.yandex.practicum.playlistmaker.ui.theme.PlaylistMakerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlaylistMakerTheme {
                MainScreen(
                    onSearchClick = {
                        val intent = Intent(this@MainActivity, SearchActivity::class.java)
                        startActivity(intent)
                    },
                    onSettingsClick = {
                        val intent = Intent(this@MainActivity, SettingsActivity::class.java)
                        startActivity(intent)
                    }
                )
            }
        }
    }
}
