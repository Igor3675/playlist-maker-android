package ru.yandex.practicum.playlistmaker.domain.model

data class Track(
    val id: String = "",
    val trackName: String,
    val artistName: String,
    val trackTime: String
)
