package ru.practicum.android.diploma.di.modules

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.practicum.android.diploma.data.db.AppDatabase
import ru.practicum.android.diploma.data.db.VacancyDao
import ru.practicum.android.diploma.data.db.converters.VacancyFavouriteDbConverters
import javax.inject.Singleton

@Module
class DatabaseModule() {
    @Singleton
    @Provides
    fun provideDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "database.db")
            .allowMainThreadQueries()
            .build()
    }

    @Singleton
    @Provides
    fun provideVacancyDao(database: AppDatabase): VacancyDao {
        return database.vacancyDao()
    }

    @Provides
    fun provideConverterFavourite(): VacancyFavouriteDbConverters {
        return VacancyFavouriteDbConverters()
    }
}
