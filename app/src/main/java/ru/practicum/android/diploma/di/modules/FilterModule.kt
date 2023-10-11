package ru.practicum.android.diploma.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.practicum.android.diploma.data.filter.FilterStorage
import ru.practicum.android.diploma.data.filter.FilterStorageImpl
import javax.inject.Singleton

@Module
class FilterModule {

    @Singleton
    @Provides
    fun provideFilterStorage(context: Context): FilterStorage{
        return FilterStorageImpl(context)
    }

}