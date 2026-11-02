package ru.yandex.practicum.playlistmaker.data.dto

data class TrackDto(
    val id: String = "",
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int
)
