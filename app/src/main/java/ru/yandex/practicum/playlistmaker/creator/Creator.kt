package ru.yandex.practicum.playlistmaker.creator

import ru.yandex.practicum.playlistmaker.data.DatabaseMock
import ru.yandex.practicum.playlistmaker.data.network.RetrofitNetworkClient
import ru.yandex.practicum.playlistmaker.data.network.Storage
import ru.yandex.practicum.playlistmaker.data.repository.PlaylistsRepositoryImpl
import ru.yandex.practicum.playlistmaker.data.repository.SearchHistoryRepositoryImpl
import ru.yandex.practicum.playlistmaker.data.repository.TracksRepositoryImpl
import ru.yandex.practicum.playlistmaker.domain.api.PlaylistsRepository
import ru.yandex.practicum.playlistmaker.domain.api.SearchHistoryRepository
import ru.yandex.practicum.playlistmaker.domain.api.TracksRepository

object Creator {
    private val storage = Storage()
    private val networkClient = RetrofitNetworkClient(storage)

    fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(networkClient, DatabaseMock)
    }

    fun getPlaylistsRepository(): PlaylistsRepository {
        return PlaylistsRepositoryImpl(DatabaseMock)
    }

    fun getSearchHistoryRepository(): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(DatabaseMock)
    }
}
