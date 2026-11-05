package ru.yandex.practicum.playlistmaker.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.yandex.practicum.playlistmaker.creator.Creator
import ru.yandex.practicum.playlistmaker.domain.api.PlaylistsRepository
import ru.yandex.practicum.playlistmaker.domain.api.TracksRepository
import ru.yandex.practicum.playlistmaker.domain.model.Playlist
import ru.yandex.practicum.playlistmaker.domain.model.Track

class PlaylistsViewModel(
    private val playlistsRepository: PlaylistsRepository = Creator.getPlaylistsRepository(),
    private val tracksRepository: TracksRepository = Creator.getTracksRepository()
) : ViewModel() {

    val playlists: Flow<List<Playlist>> = playlistsRepository.getAllPlaylists()
    val favoriteList: Flow<List<Track>> = tracksRepository.getFavoriteTracks()

    fun getPlaylist(playlistId: Long): Flow<Playlist?> {
        return playlistsRepository.getPlaylist(playlistId)
    }

    fun createNewPlayList(namePlaylist: String, description: String) {
        viewModelScope.launch {
            playlistsRepository.addNewPlaylist(namePlaylist, description)
        }
    }

    suspend fun insertSongToPlaylist(track: Track, playlistId: Long) {
        track.playlistId = playlistId
        tracksRepository.insertSongToPlaylist(track, playlistId)
    }

    fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        viewModelScope.launch {
            track.playlistId = playlist.id
            tracksRepository.insertSongToPlaylist(track, playlist.id)
        }
    }

    suspend fun toggleFavorite(track: Track, isFavorite: Boolean) {
        tracksRepository.updateTrackFavoriteStatus(track, isFavorite)
    }

    fun onFavoriteClick(track: Track, isFavorite: Boolean) {
        viewModelScope.launch {
            tracksRepository.updateTrackFavoriteStatus(track, isFavorite)
        }
    }

    fun deleteSongFromPlaylist(track: Track) {
        tracksRepository.deleteSongFromPlaylist(track)
    }

    suspend fun deletePlaylistById(id: Long) {
        tracksRepository.deleteTracksByPlaylistId(id)
        playlistsRepository.deletePlaylistById(id)
    }

    fun removePlaylist(id: Long) {
        viewModelScope.launch {
            deletePlaylistById(id)
        }
    }

    fun isExist(track: Track): Flow<Track?> {
        return tracksRepository.getTrackByNameAndArtist(track)
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                PlaylistsViewModel(
                    Creator.getPlaylistsRepository(),
                    Creator.getTracksRepository()
                )
            }
        }
    }
}
