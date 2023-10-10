package ru.practicum.android.diploma.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.data.network.dto.Response
import ru.practicum.android.diploma.data.network.dto.VacancyRequest
import javax.inject.Inject

class NetworkClientImpl @Inject constructor(
    private val hhSearchApi: HHSearchApi,
    private val context: Context
) : NetworkClient {

    override suspend fun search(dto: Any): Response {
        if (!isConnected()) {
            Log.d("tag", "-1 ")
            return Response().apply { resultCode = -1 }
        }
//        if (dto !is VacancyRequest) {
//            Log.d("tag", "-400 " + dto)
//            return Response().apply { resultCode = 400 }
//        }
        return withContext(Dispatchers.IO) {
            try {
                val response = hhSearchApi.search(dto as HashMap<String, String>)
                response.apply { resultCode = 200 }
            } catch (e: Exception) {
               e.printStackTrace()
                Response().apply { resultCode = 500 }
            }
        }
    }
//    @Query("text") expression: String,
//    @Query("page") page: Int,
//    @Query("per_page") perPage: Int

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