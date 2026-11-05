package ru.yandex.practicum.playlistmaker.data.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.yandex.practicum.playlistmaker.data.DatabaseMock
import ru.yandex.practicum.playlistmaker.data.NetworkClient
import ru.yandex.practicum.playlistmaker.data.network.RetrofitNetworkClient
import ru.yandex.practicum.playlistmaker.data.network.Storage
import ru.yandex.practicum.playlistmaker.data.repository.PlaylistsRepositoryImpl
import ru.yandex.practicum.playlistmaker.data.repository.SearchHistoryRepositoryImpl
import ru.yandex.practicum.playlistmaker.data.repository.TracksRepositoryImpl
import ru.yandex.practicum.playlistmaker.domain.api.PlaylistsRepository
import ru.yandex.practicum.playlistmaker.domain.api.SearchHistoryRepository
import ru.yandex.practicum.playlistmaker.domain.api.TracksRepository
import ru.yandex.practicum.playlistmaker.ui.view_model.PlaylistsViewModel
import ru.yandex.practicum.playlistmaker.ui.view_model.SearchViewModel

val repositoryModule = module {
    single { Storage() }
    single { DatabaseMock }
    single<NetworkClient> { RetrofitNetworkClient(get()) }
    single<TracksRepository> { TracksRepositoryImpl(get(), get()) }
    single<PlaylistsRepository> { PlaylistsRepositoryImpl(get()) }
    single<SearchHistoryRepository> { SearchHistoryRepositoryImpl(get()) }
}

val viewModelModule = module {
    viewModel { SearchViewModel(get(), get()) }
    viewModel { PlaylistsViewModel(get(), get()) }
}
