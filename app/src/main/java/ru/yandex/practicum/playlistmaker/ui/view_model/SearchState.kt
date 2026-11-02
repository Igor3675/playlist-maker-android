package ru.yandex.practicum.playlistmaker.ui.view_model

import ru.yandex.practicum.playlistmaker.domain.model.Track

sealed class SearchState {
    object Initial : SearchState()
    object Searching : SearchState()
    data class Success(val list: List<Track>) : SearchState()
    data class Fail(val error: String) : SearchState()
}
