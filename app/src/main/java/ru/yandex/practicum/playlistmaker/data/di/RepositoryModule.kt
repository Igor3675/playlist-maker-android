package ru.yandex.practicum.playlistmaker.data.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.yandex.practicum.playlistmaker.data.NetworkClient
import ru.yandex.practicum.playlistmaker.data.network.RetrofitNetworkClient
import ru.yandex.practicum.playlistmaker.data.network.Storage
import ru.yandex.practicum.playlistmaker.data.network.TracksRepositoryImpl
import ru.yandex.practicum.playlistmaker.domain.api.TrackSearchInteractor
import ru.yandex.practicum.playlistmaker.domain.api.TracksRepository
import ru.yandex.practicum.playlistmaker.domain.impl.TrackSearchInteractorImpl
import ru.yandex.practicum.playlistmaker.ui.view_model.SearchViewModel
import ru.yandex.practicum.playlistmaker.ui.view_model.TrackViewModel

val repositoryModule = module {
    single { Storage() }
    single<NetworkClient> { RetrofitNetworkClient(get()) }
    single<TracksRepository> { TracksRepositoryImpl(get()) }
    factory<TrackSearchInteractor> { TrackSearchInteractorImpl(get()) }
}

val viewModelModule = module {
    viewModel { SearchViewModel(get()) }
    viewModel { TrackViewModel() }
}
