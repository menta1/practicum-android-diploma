package ru.practicum.android.diploma.di.modules

import dagger.Binds
import dagger.Module
import ru.practicum.android.diploma.data.details.DetailsRepositoryImpl
import ru.practicum.android.diploma.data.developers.DevelopersRepositoryImpl
import ru.practicum.android.diploma.data.favourite.FavouriteRepositoryImpl
import ru.practicum.android.diploma.data.filter.FilterRepositoryImpl
import ru.practicum.android.diploma.data.search.SearchRepositoryImpl
import ru.practicum.android.diploma.data.share.SharingRepositoryImpl
import ru.practicum.android.diploma.data.similar.SimilarRepositoryImpl
import ru.practicum.android.diploma.domain.details.DetailsRepository
import ru.practicum.android.diploma.domain.developers.DevelopersRepository
import ru.practicum.android.diploma.domain.favourite.FavouriteRepository
import ru.practicum.android.diploma.domain.filter.FilterRepository
import ru.practicum.android.diploma.domain.search.SearchRepository
import ru.practicum.android.diploma.domain.share.SharingRepository
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
    @Binds
    abstract fun provideSharingRepository(repository: SharingRepositoryImpl): SharingRepository
}