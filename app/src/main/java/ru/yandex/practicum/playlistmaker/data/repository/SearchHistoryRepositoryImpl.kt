package ru.yandex.practicum.playlistmaker.data.repository

import ru.yandex.practicum.playlistmaker.data.DatabaseMock
import ru.yandex.practicum.playlistmaker.domain.api.SearchHistoryRepository
import ru.yandex.practicum.playlistmaker.domain.model.Word

class SearchHistoryRepositoryImpl(
    private val database: DatabaseMock = DatabaseMock
) : SearchHistoryRepository {

    override suspend fun getHistoryRequests(): List<String> {
        return database.getHistoryRequests()
    }

    override fun addToHistory(word: Word) {
        database.addToHistory(word)
    }
}
