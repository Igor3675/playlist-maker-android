package ru.yandex.practicum.playlistmaker.data.network

import ru.yandex.practicum.playlistmaker.data.NetworkClient
import ru.yandex.practicum.playlistmaker.data.dto.BaseResponse
import ru.yandex.practicum.playlistmaker.data.dto.TracksSearchRequest
import ru.yandex.practicum.playlistmaker.data.dto.TracksSearchResponse

class RetrofitNetworkClient(
    private val storage: Storage = Storage()
) : NetworkClient {

    override fun doRequest(dto: Any): BaseResponse {
        if (dto is TracksSearchRequest) {
            val searchList = storage.search(dto.expression)
            return TracksSearchResponse(searchList).apply { resultCode = 200 }
        }
        return BaseResponse().apply { resultCode = 400 }
    }
}
