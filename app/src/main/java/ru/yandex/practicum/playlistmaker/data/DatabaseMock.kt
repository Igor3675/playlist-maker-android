package ru.yandex.practicum.playlistmaker.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import ru.yandex.practicum.playlistmaker.domain.model.Playlist
import ru.yandex.practicum.playlistmaker.domain.model.Track
import ru.yandex.practicum.playlistmaker.domain.model.Word

object DatabaseMock {

    private val historyList = mutableListOf(
        Word("Rammstein"),
        Word("Queen"),
        Word("Bruno Mars")
    )

    private fun initialTracks(): MutableList<Track> = mutableListOf(
        Track(
            id = 1L,
            trackName = "Владивосток 2000",
            artistName = "Мумий Тролль",
            trackTime = "02:38",
            image = "https://example.com/music_1.png",
            favorite = false,
            playlistId = 1L
        ),
        Track(
            id = 2L,
            trackName = "Группа крови",
            artistName = "Кино",
            trackTime = "04:43",
            image = "https://example.com/music_2.png",
            favorite = true,
            playlistId = 2L
        ),
        Track(
            id = 3L,
            trackName = "Не смотри назад",
            artistName = "Ария",
            trackTime = "05:12",
            image = "https://example.com/music_3.png",
            favorite = false,
            playlistId = null
        ),
        Track(
            id = 4L,
            trackName = "Звезда по имени Солнце",
            artistName = "Кино",
            trackTime = "03:45",
            image = "https://example.com/music_4.png",
            favorite = true,
            playlistId = 1L
        ),
        Track(
            id = 5L,
            trackName = "Лондон",
            artistName = "Аквариум",
            trackTime = "04:32",
            image = "https://example.com/music_5.png",
            favorite = false,
            playlistId = null
        ),
        Track(
            id = 6L,
            trackName = "На заре",
            artistName = "Альянс",
            trackTime = "03:50",
            image = "https://example.com/music_6.png",
            favorite = false,
            playlistId = 1L
        ),
        Track(
            id = 7L,
            trackName = "Перемен",
            artistName = "Кино",
            trackTime = "04:56",
            image = "https://example.com/music_7.png",
            favorite = false,
            playlistId = 2L
        ),
        Track(
            id = 8L,
            trackName = "Розовый фламинго",
            artistName = "Сплин",
            trackTime = "03:15",
            image = "https://example.com/music_8.png",
            favorite = false,
            playlistId = null
        ),
        Track(
            id = 9L,
            trackName = "Танцевать",
            artistName = "Мельница",
            trackTime = "03:42",
            image = "https://example.com/music_9.png",
            favorite = false,
            playlistId = null
        ),
        Track(
            id = 10L,
            trackName = "Чёрный бумер",
            artistName = "Серёга",
            trackTime = "04:01",
            image = "https://example.com/music_10.png",
            favorite = false,
            playlistId = null
        ),
        Track(
            id = 11L,
            trackName = "Yesterday (Remastered 2009)",
            artistName = "The Beatles",
            trackTime = "02:05",
            image = "https://example.com/music_11.png",
            favorite = false,
            playlistId = 1L
        ),
        Track(
            id = 12L,
            trackName = "Let It Be (Remastered 2009)",
            artistName = "The Beatles",
            trackTime = "04:03",
            image = "https://example.com/music_12.png",
            favorite = false,
            playlistId = 2L
        )
    )

    private val tracks = initialTracks()
    private val tracksState = MutableStateFlow(tracks.toList())

    private fun initialPlaylists(currentTracks: List<Track>): MutableList<Playlist> = mutableListOf(
        Playlist(
            id = 1L,
            name = "Best songs 2021",
            description = "Tracks from 2021",
            tracks = currentTracks.filter { it.playlistId == 1L }
        ),
        Playlist(
            id = 2L,
            name = "Summer Party",
            description = "Music for summer party",
            tracks = currentTracks.filter { it.playlistId == 2L }
        )
    )

    private val playlists = initialPlaylists(tracks)
    private val playlistsState = MutableStateFlow(playlists.toList())

    private fun syncState() {
        val snapshotTracks = tracks.map { it.copy() }
        tracksState.value = snapshotTracks
        for (i in playlists.indices) {
            val pl = playlists[i]
            playlists[i] = pl.copy(tracks = snapshotTracks.filter { it.playlistId == pl.id })
        }
        playlistsState.value = playlists.map { it.copy() }
    }

    fun reset() {
        historyList.clear()
        historyList.addAll(listOf(Word("Rammstein"), Word("Queen"), Word("Bruno Mars")))
        tracks.clear()
        tracks.addAll(initialTracks())
        playlists.clear()
        playlists.addAll(initialPlaylists(tracks))
        syncState()
    }

    fun getAllTracks(): List<Track> = tracks.map { it.copy() }

    fun getHistoryRequests(): List<String> = historyList.map { it.word }

    fun addToHistory(word: Word) {
        val trimmed = word.word.trim()
        if (trimmed.isEmpty()) return
        historyList.removeAll { it.word.equals(trimmed, ignoreCase = true) }
        historyList.add(0, Word(trimmed))
    }

    fun getPlaylist(playlistId: Long): Flow<Playlist?> {
        return playlistsState.map { list ->
            list.find { it.id == playlistId }
        }
    }

    fun getAllPlaylists(): Flow<List<Playlist>> = playlistsState

    fun addNewPlaylist(name: String, description: String) {
        val nextId = (playlists.maxOfOrNull { it.id } ?: 0L) + 1L
        playlists.add(
            Playlist(
                id = nextId,
                name = name,
                description = description,
                tracks = emptyList()
            )
        )
        syncState()
    }

    fun deletePlaylistById(id: Long) {
        tracks.forEachIndexed { index, track ->
            if (track.playlistId == id) {
                tracks[index] = track.copy(playlistId = null)
            }
        }
        playlists.removeIf { it.id == id }
        syncState()
    }

    fun getTrackByNameAndArtist(track: Track): Flow<Track?> {
        return tracksState.map { list ->
            list.find { it.trackName == track.trackName && it.artistName == track.artistName }
        }
    }

    fun getFavoriteTracks(): Flow<List<Track>> {
        return tracksState.map { list ->
            list.filter { it.favorite }
        }
    }

    fun updateTrackFavoriteStatus(track: Track, isFavorite: Boolean) {
        val index = tracks.indexOfFirst {
            it.trackName == track.trackName && it.artistName == track.artistName
        }
        track.favorite = isFavorite
        if (index >= 0) {
            tracks[index] = tracks[index].copy(favorite = isFavorite)
        } else {
            val nextId = (tracks.maxOfOrNull { it.id } ?: 0L) + 1L
            tracks.add(track.copy(id = if (track.id > 0L) track.id else nextId, favorite = isFavorite))
        }
        syncState()
    }

    fun insertSongToPlaylist(track: Track, playlistId: Long) {
        track.playlistId = playlistId
        val index = tracks.indexOfFirst {
            it.trackName == track.trackName && it.artistName == track.artistName
        }
        if (index >= 0) {
            tracks[index] = tracks[index].copy(playlistId = playlistId)
        } else {
            val nextId = (tracks.maxOfOrNull { it.id } ?: 0L) + 1L
            tracks.add(track.copy(id = if (track.id > 0L) track.id else nextId, playlistId = playlistId))
        }
        syncState()
    }

    fun deleteSongFromPlaylist(track: Track) {
        track.playlistId = null
        val index = tracks.indexOfFirst {
            it.trackName == track.trackName && it.artistName == track.artistName
        }
        if (index >= 0) {
            tracks[index] = tracks[index].copy(playlistId = null)
        }
        syncState()
    }

    fun deleteTracksByPlaylistId(playlistId: Long) {
        for (i in tracks.indices) {
            if (tracks[i].playlistId == playlistId) {
                tracks[i] = tracks[i].copy(playlistId = null)
            }
        }
        syncState()
    }
}
