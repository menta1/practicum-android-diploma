package ru.practicum.android.diploma.data.network

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.data.network.dto.VacancyResponse

interface HHSearchApi {
    @Headers(
        "Authorization: Bearer $token",
        "HH-User-Agent: Talent Trove (bulatov.aynur@yandex.ru)"
    )
    @GET("/vacancies")
    suspend fun search(
        @Query("text") expression: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): VacancyResponse

    companion object {
        const val token = BuildConfig.HH_ACCESS_TOKEN
    }
}