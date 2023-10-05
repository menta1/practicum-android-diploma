package ru.practicum.android.diploma.di.modules

import dagger.Module
import dagger.Provides
import ru.practicum.android.diploma.data.network.converters.FiltersNetworkConverter

@Module
class NetworkModule {

    @Provides
    fun provideFiltersNetworkConverter(): FiltersNetworkConverter {
        return FiltersNetworkConverter()
    }
}