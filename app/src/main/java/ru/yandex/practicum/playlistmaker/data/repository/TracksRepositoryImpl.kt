package ru.yandex.practicum.playlistmaker.data.repository

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import ru.yandex.practicum.playlistmaker.data.DatabaseMock
import ru.yandex.practicum.playlistmaker.data.NetworkClient
import ru.yandex.practicum.playlistmaker.data.dto.TracksSearchRequest
import ru.yandex.practicum.playlistmaker.data.dto.TracksSearchResponse
import ru.yandex.practicum.playlistmaker.domain.api.TracksRepository
import ru.yandex.practicum.playlistmaker.domain.model.Track
import java.util.Locale

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val database: DatabaseMock = DatabaseMock,
    private val simulatedDelayMs: Long = 500L
) : TracksRepository {

    override suspend fun searchTracks(expression: String): List<Track> {
        if (simulatedDelayMs > 0) {
            delay(simulatedDelayMs)
        }
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        return if (response.resultCode == 200) {
            val dbTracks = database.getAllTracks()
            (response as TracksSearchResponse).results.mapIndexed { index, dto ->
                val minutes = dto.trackTimeMillis / 60000
                val seconds = (dto.trackTimeMillis % 60000) / 1000
                val formattedTime = String.format(Locale.US, "%02d:%02d", minutes, seconds)
                val existing = dbTracks.find {
                    it.trackName == dto.trackName && it.artistName == dto.artistName
                }
                Track(
                    id = existing?.id ?: (dto.id.toLongOrNull() ?: (index + 1L)),
                    trackName = dto.trackName,
                    artistName = dto.artistName,
                    trackTime = formattedTime,
                    image = existing?.image ?: "",
                    favorite = existing?.favorite ?: false,
                    playlistId = existing?.playlistId
                )
            }
        } else {
            throw Exception("Server error: ${response.resultCode}")
        }
    }

    override fun getTrackByNameAndArtist(track: Track): Flow<Track?> {
        return database.getTrackByNameAndArtist(track)
    }

    override fun getFavoriteTracks(): Flow<List<Track>> {
        return database.getFavoriteTracks()
    }

    override suspend fun updateTrackFavoriteStatus(track: Track, isFavorite: Boolean) {
        database.updateTrackFavoriteStatus(track, isFavorite)
    }

    override suspend fun deleteTracksByPlaylistId(playlistId: Long) {
        database.deleteTracksByPlaylistId(playlistId)
    }

    override suspend fun insertSongToPlaylist(track: Track, playlistId: Long) {
        database.insertSongToPlaylist(track, playlistId)
    }

    override fun deleteSongFromPlaylist(track: Track) {
        database.deleteSongFromPlaylist(track)
    }
}
