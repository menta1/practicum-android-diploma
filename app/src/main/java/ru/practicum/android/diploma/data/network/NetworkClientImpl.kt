package ru.practicum.android.diploma.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.data.Constants.NO_INTERNET
import ru.practicum.android.diploma.data.Constants.OK_RESPONSE
import ru.practicum.android.diploma.data.Constants.SERVER_ERROR
import ru.practicum.android.diploma.data.network.dto.IndustryResponse
import ru.practicum.android.diploma.data.network.dto.RegionResponse
import ru.practicum.android.diploma.data.network.dto.Response
import ru.practicum.android.diploma.data.network.dto.SingleRegionResponse
import ru.practicum.android.diploma.data.network.dto.VacancyDetailResponse
import ru.practicum.android.diploma.data.network.dto.VacancyRequest
import javax.inject.Inject

class NetworkClientImpl @Inject constructor(
    private val hhSearchApi: HHSearchApi,
    private val context: Context
) : NetworkClient {

    override suspend fun search(dto: VacancyRequest): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = NO_INTERNET }
        }
        return withContext(Dispatchers.IO) {
            try {
                val response = hhSearchApi.search(dto.request)
                response.apply { resultCode = OK_RESPONSE }
            } catch (e: Exception) {
                e.printStackTrace()
                Response().apply { resultCode = SERVER_ERROR }
            }
        }
    }

    override suspend fun getAllCountries(): Response {

        if (!isConnected()) {
            return Response().apply { resultCode = NO_INTERNET }
        }
        val response = hhSearchApi.getAllCountries()

        return if (response.code() == OK_RESPONSE && response.body() != null) {
            RegionResponse(results = response.body()!!).apply { resultCode = OK_RESPONSE }
        } else {
            Response().apply { resultCode = response.code() }
        }
    }

    override suspend fun getAllRegionsInCountry(countryId: String): Response {

        if (!isConnected()) {
            return Response().apply { resultCode = NO_INTERNET }
        }
        val response = hhSearchApi.getAllRegionsInCountry(countryId)

        return if (response.code() == OK_RESPONSE && response.body() != null) {
            SingleRegionResponse(results = response.body()!!).apply { resultCode = OK_RESPONSE }
        } else {
            Response().apply { resultCode = response.code() }
        }
    }

    override suspend fun getAllIndustries(): Response {

        if (!isConnected()) {
            return Response().apply { resultCode = NO_INTERNET }
        }
        val response = hhSearchApi.getAllIndustries()

        return if (response.code() == OK_RESPONSE && response.body() != null) {
            IndustryResponse(results = response.body()!!).apply { resultCode = OK_RESPONSE }
        } else {
            Response().apply { resultCode = response.code() }
        }
    }

    override suspend fun getVacancyDetail(vacancyId: String): Response {
        val response = hhSearchApi.getVacancyDetail(vacancyId)

        if (!isConnected()) {
            return Response().apply { resultCode = NO_INTERNET }
        }
        return if (response.code() == OK_RESPONSE && response.body() != null) {
            VacancyDetailResponse(result = response.body()!!).apply { resultCode = OK_RESPONSE }
        } else {
            Response().apply { resultCode = response.code() }
        }
    }

    override suspend fun getSimilarVacancy(vacancyId: String, dto: VacancyRequest): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = NO_INTERNET }
        }
        return withContext(Dispatchers.IO) {
            try {
                val response = hhSearchApi.getSimilarVacancy(vacancyId, dto.request)
                response.apply { resultCode = OK_RESPONSE }
            } catch (e: Exception) {
                e.printStackTrace()
                Response().apply { resultCode = SERVER_ERROR }
            }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}