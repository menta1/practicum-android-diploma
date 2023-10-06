package ru.practicum.android.diploma.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.data.network.HHSearchApi
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.NetworkClientImpl

@Module
class NetworkModule {

    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.hh.ru")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideApiService(retrofit: Retrofit): HHSearchApi {
        return retrofit.create(HHSearchApi::class.java)
    }

    @Provides
    fun provideNetworkClient(context: Context, apiService: HHSearchApi): NetworkClient {
        return NetworkClientImpl(apiService, context)
    }
}