package ru.practicum.android.diploma.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.QueryMap
import retrofit2.http.Path
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.data.network.dto.IndustryDto
import ru.practicum.android.diploma.data.network.dto.RegionDto
import ru.practicum.android.diploma.data.network.dto.VacancyDetailDto
import ru.practicum.android.diploma.data.network.dto.VacancyResponse

interface HHSearchApi {
    @Headers(
        "Authorization: Bearer $token", "HH-User-Agent: Talent Trove (bulatov.aynur@yandex.ru)"
    )
    @GET("/vacancies")
    suspend fun search(@QueryMap options: Map<String, String>): VacancyResponse

    @GET("/areas")
    suspend fun getAllCountries(): Response<List<RegionDto>>

    @GET("/areas/{countryId}")
    suspend fun getAllRegionsInCountry(
        @Path("countryId") countryId: String
    ): Response<List<RegionDto>>

    @GET("/industries")
    suspend fun getAllIndustries(): Response<List<IndustryDto>>

    @GET("/vacancies/{vacancy_id}")
    suspend fun getVacancyDetail(
        @Path("vacancy_id") vacancyId: String
    ): Response<VacancyDetailDto>

    @Headers(
        "Authorization: Bearer $token", "HH-User-Agent: Talent Trove (bulatov.aynur@yandex.ru)"
    )
    @GET("/vacancies/{vacancy_id}/similar_vacancies")
    suspend fun getSimilarVacancy(
        @Path("vacancy_id") vacancyId: String,
        @QueryMap options: Map<String, String>
    ): VacancyResponse

    companion object {
        const val token = BuildConfig.HH_ACCESS_TOKEN
    }
//    @Query("text") expression: String,
//    @Query("page") page: Int,
//    @Query("per_page") perPage: Int
}