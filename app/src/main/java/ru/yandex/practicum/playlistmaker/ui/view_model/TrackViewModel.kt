package ru.yandex.practicum.playlistmaker.ui.view_model

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.yandex.practicum.playlistmaker.domain.model.Track

class TrackViewModel : ViewModel() {
    private val _selectedTrack = MutableStateFlow<Track?>(null)
    val selectedTrack: StateFlow<Track?> = _selectedTrack.asStateFlow()

    fun selectTrack(track: Track) {
        _selectedTrack.value = track
    }
}
