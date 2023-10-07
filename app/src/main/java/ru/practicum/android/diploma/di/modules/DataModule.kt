package ru.practicum.android.diploma.di.modules

import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.data.details.DetailsRepositoryImpl
import ru.practicum.android.diploma.data.developers.DevelopersRepositoryImpl
import ru.practicum.android.diploma.data.favourite.FavouriteRepositoryImpl
import ru.practicum.android.diploma.data.filter.FilterRepositoryImpl
import ru.practicum.android.diploma.data.network.HHSearchApi
import ru.practicum.android.diploma.data.network.converters.FiltersNetworkConverter
import ru.practicum.android.diploma.data.search.SearchRepositoryImpl
import ru.practicum.android.diploma.data.similar.SimilarRepositoryImpl
import ru.practicum.android.diploma.domain.details.DetailsRepository
import ru.practicum.android.diploma.domain.developers.DevelopersRepository
import ru.practicum.android.diploma.domain.favourite.FavouriteRepository
import ru.practicum.android.diploma.domain.filter.FilterRepository
import ru.practicum.android.diploma.domain.search.SearchRepository
import ru.practicum.android.diploma.domain.similar.SimilarRepository

@Module
abstract class DataModule {
    @Binds
    abstract fun provideDetailsRepository(repository: DetailsRepositoryImpl): DetailsRepository
    @Binds
    abstract fun provideDevelopersRepository(repository: DevelopersRepositoryImpl): DevelopersRepository
    @Binds
    abstract fun provideFavouriteRepository(repository: FavouriteRepositoryImpl): FavouriteRepository
    @Binds
    abstract fun provideFilterRepository(repository: FilterRepositoryImpl): FilterRepository
    @Binds
    abstract fun provideSearchRepository(repository: SearchRepositoryImpl): SearchRepository
    @Binds
    abstract fun provideSimilarRepository(repository: SimilarRepositoryImpl): SimilarRepository


}