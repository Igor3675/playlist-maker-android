package ru.yandex.practicum.playlistmaker.creator

import ru.yandex.practicum.playlistmaker.data.network.RetrofitNetworkClient
import ru.yandex.practicum.playlistmaker.data.network.Storage
import ru.yandex.practicum.playlistmaker.data.network.TracksRepositoryImpl
import ru.yandex.practicum.playlistmaker.domain.api.TrackSearchInteractor
import ru.yandex.practicum.playlistmaker.domain.api.TracksRepository
import ru.yandex.practicum.playlistmaker.domain.impl.TrackSearchInteractorImpl

object Creator {
    fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient(Storage()))
    }

    fun provideTrackSearchInteractor(): TrackSearchInteractor {
        return TrackSearchInteractorImpl(getTracksRepository())
    }
}
