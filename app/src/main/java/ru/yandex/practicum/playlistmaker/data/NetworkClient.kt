package ru.yandex.practicum.playlistmaker.data

import ru.yandex.practicum.playlistmaker.data.dto.BaseResponse

interface NetworkClient {
    fun doRequest(dto: Any): BaseResponse
}
