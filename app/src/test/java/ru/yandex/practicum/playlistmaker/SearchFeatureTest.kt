package ru.yandex.practicum.playlistmaker

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import ru.yandex.practicum.playlistmaker.data.network.RetrofitNetworkClient
import ru.yandex.practicum.playlistmaker.data.network.Storage
import ru.yandex.practicum.playlistmaker.data.network.TracksRepositoryImpl
import ru.yandex.practicum.playlistmaker.domain.api.TracksRepository
import ru.yandex.practicum.playlistmaker.domain.impl.TrackSearchInteractorImpl
import ru.yandex.practicum.playlistmaker.domain.model.Track
import ru.yandex.practicum.playlistmaker.ui.view_model.SearchState
import ru.yandex.practicum.playlistmaker.ui.view_model.SearchViewModel
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class SearchFeatureTest {

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
    fun `interactor delegates search to repository`() = runTest {
        val repository = TracksRepositoryImpl(
            networkClient = RetrofitNetworkClient(Storage()),
            simulatedDelayMs = 0L
        )
        val interactor = TrackSearchInteractorImpl(repository)
        val results = interactor.searchTracks("Billie Jean")
        assertEquals(1, results.size)
        assertEquals("Michael Jackson", results.first().artistName)
        assertEquals("04:54", results.first().trackTime)
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
}
