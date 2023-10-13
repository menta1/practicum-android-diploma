package ru.practicum.android.diploma.data.network


import ru.practicum.android.diploma.data.network.dto.Response

interface NetworkClient {
    suspend fun search(dto: Any): Response

    suspend fun getAllCountries():  Response
    suspend fun getAllRegionsInCountry(countryId: String): Response

    suspend fun getAllIndustries(): Response

    suspend fun getVacancyDetail(vacancyId:String): Response
}