package ru.practicum.android.diploma.data.network

import ru.practicum.android.diploma.data.network.dto.Response

interface NetworkClient {
    suspend fun search(dto: Any): Response
}