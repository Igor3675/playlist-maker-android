package ru.yandex.practicum.playlistmaker.domain.api

import ru.yandex.practicum.playlistmaker.domain.model.Track

interface TrackSearchInteractor {
    suspend fun searchTracks(expression: String): List<Track>
}
