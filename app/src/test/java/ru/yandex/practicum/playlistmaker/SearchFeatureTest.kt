package ru.yandex.practicum.playlistmaker

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import ru.yandex.practicum.playlistmaker.data.DatabaseMock
import ru.yandex.practicum.playlistmaker.data.network.RetrofitNetworkClient
import ru.yandex.practicum.playlistmaker.data.network.Storage
import ru.yandex.practicum.playlistmaker.data.repository.PlaylistsRepositoryImpl
import ru.yandex.practicum.playlistmaker.data.repository.SearchHistoryRepositoryImpl
import ru.yandex.practicum.playlistmaker.data.repository.TracksRepositoryImpl
import ru.yandex.practicum.playlistmaker.domain.api.TracksRepository
import ru.yandex.practicum.playlistmaker.domain.model.Track
import ru.yandex.practicum.playlistmaker.domain.model.Word
import ru.yandex.practicum.playlistmaker.ui.view_model.PlaylistsViewModel
import ru.yandex.practicum.playlistmaker.ui.view_model.SearchState
import ru.yandex.practicum.playlistmaker.ui.view_model.SearchViewModel
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class SearchFeatureTest {

    @Before
    fun setUp() {
        DatabaseMock.reset()
    }

    @Test
    fun `storage filters tracks case insensitively by trackName and artistName`() {
        val storage = Storage()
        val kinoResults = storage.search("кИнО")
        assertEquals(2, kinoResults.size)

        val beatlesResults = storage.search("the beatles")
        assertEquals(5, beatlesResults.size)

        val nirvanaResults = storage.search("smells like")
        assertEquals(1, nirvanaResults.size)
        assertEquals("Nirvana", nirvanaResults.first().artistName)
    }

    @Test
    fun `repository formats trackTimeMillis into mm ss`() = runTest {
        val repository = TracksRepositoryImpl(
            networkClient = RetrofitNetworkClient(Storage()),
            simulatedDelayMs = 0L
        )
        val results = repository.searchTracks("Владивосток")
        assertEquals(1, results.size)
        assertEquals("02:38", results.first().trackTime)
    }

    @Test
    fun `viewModel transitions from Initial to Success and clears back to Initial`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val repository = TracksRepositoryImpl(
            networkClient = RetrofitNetworkClient(Storage()),
            simulatedDelayMs = 0L
        )
        val viewModel = SearchViewModel(
            tracksRepository = repository,
            ioDispatcher = dispatcher
        )

        assertTrue(viewModel.searchScreenState.value is SearchState.Initial)

        viewModel.search("Кино")
        testScheduler.advanceUntilIdle()

        val state = viewModel.searchScreenState.value
        assertTrue(state is SearchState.Success)
        assertEquals(2, (state as SearchState.Success).list.size)

        viewModel.clearSearch()
        assertTrue(viewModel.searchScreenState.value is SearchState.Initial)
    }

    @Test
    fun `viewModel transitions to Fail when repository throws IOException`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val failingRepository = object : TracksRepository {
            override suspend fun searchTracks(expression: String): List<Track> {
                throw IOException("Network unreachable")
            }
        }
        val viewModel = SearchViewModel(
            tracksRepository = failingRepository,
            ioDispatcher = dispatcher
        )

        viewModel.search("Beatles")
        testScheduler.advanceUntilIdle()

        val state = viewModel.searchScreenState.value
        assertTrue(state is SearchState.Fail)
        assertEquals("Network unreachable", (state as SearchState.Fail).error)
    }

    @Test
    fun `searchHistoryRepository stores new queries at the top without duplicates`() = runTest {
        val historyRepo = SearchHistoryRepositoryImpl(DatabaseMock)
        historyRepo.addToHistory(Word("Metallica"))
        val history = historyRepo.getHistoryRequests()
        assertEquals("Metallica", history.first())
    }

    @Test
    fun `playlistsViewModel adds track to playlist and toggles favorite status`() = runTest {
        val playlistsRepo = PlaylistsRepositoryImpl(DatabaseMock)
        val tracksRepo = TracksRepositoryImpl(RetrofitNetworkClient(Storage()), DatabaseMock, 0L)
        val viewModel = PlaylistsViewModel(playlistsRepo, tracksRepo)

        val track = Track(id = 3L, trackName = "Не смотри назад", artistName = "Ария", trackTime = "05:12")
        viewModel.insertSongToPlaylist(track, 2L)
        assertEquals(2L, track.playlistId)

        val summerParty = viewModel.getPlaylist(2L).first()
        assertNotNull(summerParty)
        assertTrue(summerParty!!.tracks.any { it.trackName == "Не смотри назад" })

        viewModel.toggleFavorite(track, true)
        val favorites = viewModel.favoriteList.first()
        assertTrue(favorites.any { it.trackName == "Не смотри назад" })

        viewModel.deletePlaylistById(2L)
        assertNull(viewModel.getPlaylist(2L).first())
        val updatedTrack = viewModel.isExist(track).first()
        assertFalse(updatedTrack?.playlistId == 2L)
    }
}
