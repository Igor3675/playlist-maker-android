package ru.yandex.practicum.playlistmaker.data.network

import ru.yandex.practicum.playlistmaker.data.dto.TrackDto

class Storage {
    private val listTracks = listOf(
        TrackDto(
            id = "1",
            trackName = "Владивосток 2000",
            artistName = "Мумий Тролль",
            trackTimeMillis = 158000
        ),
        TrackDto(
            id = "2",
            trackName = "Группа крови",
            artistName = "Кино",
            trackTimeMillis = 283000
        ),
        TrackDto(
            id = "3",
            trackName = "Не смотри назад",
            artistName = "Ария",
            trackTimeMillis = 312000
        ),
        TrackDto(
            id = "4",
            trackName = "Звезда по имени Солнце",
            artistName = "Кино",
            trackTimeMillis = 225000
        ),
        TrackDto(
            id = "5",
            trackName = "Ланцеты",
            artistName = "Би-2",
            trackTimeMillis = 256000
        ),
        TrackDto(
            id = "6",
            trackName = "Smells Like Teen Spirit",
            artistName = "Nirvana",
            trackTimeMillis = 301000
        ),
        TrackDto(
            id = "7",
            trackName = "Billie Jean",
            artistName = "Michael Jackson",
            trackTimeMillis = 294000
        ),
        TrackDto(
            id = "8",
            trackName = "Stayin' Alive",
            artistName = "Bee Gees",
            trackTimeMillis = 284000
        ),
        TrackDto(
            id = "9",
            trackName = "Whole Lotta Love",
            artistName = "Led Zeppelin",
            trackTimeMillis = 333000
        ),
        TrackDto(
            id = "10",
            trackName = "Sweet Child O'Mine",
            artistName = "Guns N' Roses",
            trackTimeMillis = 303000
        ),
        TrackDto(
            id = "11",
            trackName = "Here Comes The Sun",
            artistName = "The Beatles",
            trackTimeMillis = 185000
        ),
        TrackDto(
            id = "12",
            trackName = "Yesterday",
            artistName = "The Beatles",
            trackTimeMillis = 125000
        ),
        TrackDto(
            id = "13",
            trackName = "Let It Be",
            artistName = "The Beatles",
            trackTimeMillis = 243000
        ),
        TrackDto(
            id = "14",
            trackName = "Hey Jude",
            artistName = "The Beatles",
            trackTimeMillis = 431000
        ),
        TrackDto(
            id = "15",
            trackName = "Come Together",
            artistName = "The Beatles",
            trackTimeMillis = 259000
        )
    )

    fun search(request: String): List<TrackDto> {
        val query = request.trim()
        if (query.isEmpty()) return emptyList()
        return listTracks.filter {
            it.trackName.contains(query, ignoreCase = true) ||
                it.artistName.contains(query, ignoreCase = true)
        }
    }
}
