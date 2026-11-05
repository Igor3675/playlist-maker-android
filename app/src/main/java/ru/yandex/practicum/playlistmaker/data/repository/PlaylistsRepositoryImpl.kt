package ru.yandex.practicum.playlistmaker.data.repository

import kotlinx.coroutines.flow.Flow
import ru.yandex.practicum.playlistmaker.data.DatabaseMock
import ru.yandex.practicum.playlistmaker.domain.api.PlaylistsRepository
import ru.yandex.practicum.playlistmaker.domain.model.Playlist

class PlaylistsRepositoryImpl(
    private val database: DatabaseMock = DatabaseMock
) : PlaylistsRepository {

    override fun getPlaylist(playlistId: Long): Flow<Playlist?> {
        return database.getPlaylist(playlistId)
    }

    override fun getAllPlaylists(): Flow<List<Playlist>> {
        return database.getAllPlaylists()
    }

    override suspend fun addNewPlaylist(name: String, description: String) {
        database.addNewPlaylist(name, description)
    }

    override suspend fun deletePlaylistById(id: Long) {
        database.deletePlaylistById(id)
    }
}
