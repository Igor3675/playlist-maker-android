package ru.yandex.practicum.playlistmaker.domain.api

import ru.yandex.practicum.playlistmaker.domain.model.Word

interface SearchHistoryRepository {
    suspend fun getHistoryRequests(): List<String>
    fun addToHistory(word: Word)
}
