package ru.yandex.practicum.playlistmaker.domain.api

import kotlinx.coroutines.flow.Flow
import ru.yandex.practicum.playlistmaker.domain.model.Playlist

interface PlaylistsRepository {
    fun getPlaylist(playlistId: Long): Flow<Playlist?>
    fun getAllPlaylists(): Flow<List<Playlist>>
    suspend fun addNewPlaylist(name: String, description: String)
    suspend fun deletePlaylistById(id: Long)
}
