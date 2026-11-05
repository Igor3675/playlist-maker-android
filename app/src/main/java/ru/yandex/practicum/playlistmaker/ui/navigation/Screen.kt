package ru.yandex.practicum.playlistmaker.ui.navigation

enum class Screen(val route: String) {
    MAIN("main"),
    SEARCH("search"),
    SETTINGS("settings"),
    PLAYLISTS("playlists"),
    CREATE_PLAYLIST("create_playlist"),
    PLAYLIST_DETAILS("playlist/{playlistId}"),
    FAVORITES("favorites"),
    DETAILS("details/{trackName}/{artistName}/{trackTime}")
}
