package ru.yandex.practicum.playlistmaker.data.network

import ru.yandex.practicum.playlistmaker.data.dto.TrackDto

class Storage {
    private val listTracks = listOf(
        TrackDto(
            id = "1",
            trackName = "Vladivostok 2000",
            artistName = "Mumiy Troll",
            trackTimeMillis = 158000
        ),
        TrackDto(
            id = "2",
            trackName = "Gruppa krovi",
            artistName = "Kino",
            trackTimeMillis = 283000
        ),
        TrackDto(
            id = "3",
            trackName = "Ne smotri nazad",
            artistName = "Aria",
            trackTimeMillis = 312000
        ),
        TrackDto(
            id = "4",
            trackName = "Zvezda po imeni Solntse",
            artistName = "Kino",
            trackTimeMillis = 225000
        ),
        TrackDto(
            id = "5",
            trackName = "Lantsety",
            artistName = "B-2",
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
