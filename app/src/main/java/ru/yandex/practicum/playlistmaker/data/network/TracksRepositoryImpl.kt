package ru.yandex.practicum.playlistmaker.data.network

import kotlinx.coroutines.delay
import ru.yandex.practicum.playlistmaker.data.NetworkClient
import ru.yandex.practicum.playlistmaker.data.dto.TracksSearchRequest
import ru.yandex.practicum.playlistmaker.data.dto.TracksSearchResponse
import ru.yandex.practicum.playlistmaker.domain.api.TracksRepository
import ru.yandex.practicum.playlistmaker.domain.model.Track
import java.util.Locale

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val simulatedDelayMs: Long = 1000L
) : TracksRepository {

    override suspend fun searchTracks(expression: String): List<Track> {
        if (simulatedDelayMs > 0) {
            delay(simulatedDelayMs)
        }
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        return if (response.resultCode == 200) {
            (response as TracksSearchResponse).results.map {
                val seconds = it.trackTimeMillis / 1000
                val minutes = seconds / 60
                val trackTime = String.format(Locale.US, "%02d:%02d", minutes, seconds - minutes * 60)
                Track(
                    id = it.id,
                    trackName = it.trackName,
                    artistName = it.artistName,
                    trackTime = trackTime
                )
            }
        } else {
            emptyList()
        }
    }
}
