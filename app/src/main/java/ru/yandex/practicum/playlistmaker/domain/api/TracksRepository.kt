package ru.yandex.practicum.playlistmaker.domain.api

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ru.yandex.practicum.playlistmaker.domain.model.Track

interface TracksRepository {
    suspend fun searchTracks(expression: String): List<Track>
    fun getTrackByNameAndArtist(track: Track): Flow<Track?> = emptyFlow()
    fun getFavoriteTracks(): Flow<List<Track>> = emptyFlow()
    suspend fun updateTrackFavoriteStatus(track: Track, isFavorite: Boolean) {}
    suspend fun deleteTracksByPlaylistId(playlistId: Long) {}
    suspend fun insertSongToPlaylist(track: Track, playlistId: Long) {}
    fun deleteSongFromPlaylist(track: Track) {}
}
