package ru.yandex.practicum.playlistmaker.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import ru.yandex.practicum.playlistmaker.creator.Creator
import ru.yandex.practicum.playlistmaker.domain.api.SearchHistoryRepository
import ru.yandex.practicum.playlistmaker.domain.api.TracksRepository
import ru.yandex.practicum.playlistmaker.domain.model.Word
import java.io.IOException

@OptIn(FlowPreview::class)
class SearchViewModel(
    private val tracksRepository: TracksRepository,
    private val searchHistoryRepository: SearchHistoryRepository = Creator.getSearchHistoryRepository(),
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _searchScreenState = MutableStateFlow<SearchState>(SearchState.Initial)
    val searchScreenState: StateFlow<SearchState> = _searchScreenState.asStateFlow()

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _historyList = MutableStateFlow<List<String>>(emptyList())
    val historyList: StateFlow<List<String>> = _historyList.asStateFlow()

    private var lastSearchQuery: String = ""
    private var searchJob: Job? = null

    init {
        viewModelScope.launch(ioDispatcher) {
            _query
                .debounce(1000L)
                .distinctUntilChanged()
                .collect { queryText ->
                    if (queryText.isNotBlank()) {
                        search(queryText)
                    }
                }
        }
    }

    fun updateQuery(newQuery: String) {
        _query.value = newQuery
        if (newQuery.isBlank()) {
            clearSearch()
        }
    }

    fun search(whatSearch: String) {
        val trimmed = whatSearch.trim()
        if (trimmed.isEmpty()) {
            clearSearch()
            return
        }
        lastSearchQuery = trimmed
        searchJob?.cancel()
        searchJob = viewModelScope.launch(ioDispatcher) {
            try {
                _searchScreenState.value = SearchState.Searching
                searchHistoryRepository.addToHistory(Word(trimmed))
                _historyList.value = searchHistoryRepository.getHistoryRequests()
                val list = tracksRepository.searchTracks(expression = trimmed)
                _searchScreenState.value = SearchState.Success(list = list)
            } catch (e: IOException) {
                _searchScreenState.value = SearchState.Fail(e.message.toString())
            } catch (e: Exception) {
                _searchScreenState.value = SearchState.Fail(e.message.toString())
            }
        }
    }

    fun retryLastSearch() {
        if (lastSearchQuery.isNotEmpty()) {
            search(lastSearchQuery)
        }
    }

    fun clearSearch() {
        searchJob?.cancel()
        lastSearchQuery = ""
        _searchScreenState.value = SearchState.Initial
    }

    suspend fun getHistoryList(): List<String> {
        val list = searchHistoryRepository.getHistoryRequests()
        _historyList.value = list
        return list
    }

    fun loadHistory() {
        viewModelScope.launch(ioDispatcher) {
            _historyList.value = searchHistoryRepository.getHistoryRequests()
        }
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchViewModel(
                    Creator.getTracksRepository(),
                    Creator.getSearchHistoryRepository()
                )
            }
        }
    }
}
