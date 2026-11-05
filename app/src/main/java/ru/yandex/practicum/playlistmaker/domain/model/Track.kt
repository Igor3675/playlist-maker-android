package ru.yandex.practicum.playlistmaker.domain.model

data class Track(
    val id: Long = 0L,
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val image: String = "",
    var favorite: Boolean = false,
    var playlistId: Long? = null
)
