package ru.yandex.practicum.playlistmaker

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.yandex.practicum.playlistmaker.data.di.repositoryModule
import ru.yandex.practicum.playlistmaker.data.di.viewModelModule

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            modules(repositoryModule, viewModelModule)
        }
    }
}
