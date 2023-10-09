package ru.practicum.android.diploma.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.data.network.dto.IndustryDto
import ru.practicum.android.diploma.data.network.dto.IndustryResponse
import ru.practicum.android.diploma.data.network.dto.RegionDto
import ru.practicum.android.diploma.data.network.dto.RegionResponse
import ru.practicum.android.diploma.data.network.dto.Response
import ru.practicum.android.diploma.data.network.dto.VacancyRequest
import javax.inject.Inject

class NetworkClientImpl @Inject constructor(
    private val hhSearchApi: HHSearchApi,
    private val context: Context
) : NetworkClient {

    override suspend fun search(dto: Any): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = -1 }
        }
        if (dto !is VacancyRequest) {
            return Response().apply { resultCode = 400 }
        }
        return withContext(Dispatchers.IO) {
            try {
                val response = hhSearchApi.search(dto.expression, dto.page, dto.pageSize)
                response.apply { resultCode = 200 }
            } catch (e: Exception) {
                Response().apply { resultCode = 500 }
            }
        }
    }

    override suspend fun getAllCountries(): Response {

        val response = hhSearchApi.getAllCountries()

        if (!isConnected()){
            return Response().apply { resultCode = -1 }
        }
        return if (response.code()==200 && response.body() != null){
            RegionResponse(results = response.body()!!).apply { resultCode = 200 }
        } else{
            Response().apply { resultCode = response.code() }
        }
    }

    override suspend fun getAllRegionsInCountry(countryId: String): Response {

        val response = hhSearchApi.getAllRegionsInCountry(countryId)

        if (!isConnected()){
            return Response().apply { resultCode = -1 }
        }
        return if (response.code()==200 && response.body() != null){
            RegionResponse(results = response.body()!! ).apply { resultCode = 200 }
        } else{
            Response().apply { resultCode = response.code() }
        }
    }

    override suspend fun getAllIndustries(): Response {

        val response = hhSearchApi.getAllIndustries()

        if (!isConnected()){
            return Response().apply { resultCode = -1 }
        }
        return if (response.code()==200 && response.body() != null){
            IndustryResponse(results = response.body()!! ).apply { resultCode = 200 }
        } else{
            Response().apply { resultCode = response.code() }
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