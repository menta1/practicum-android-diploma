package ru.practicum.android.diploma.di.modules

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.practicum.android.diploma.data.db.AppDatabase
import ru.practicum.android.diploma.data.db.VacancyDao
import ru.practicum.android.diploma.data.db.converters.VacancyFavouriteDbConverters

@Module
class DatabaseModule {
    @Provides
    fun provideAppDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "database.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideVacancyDao(db: AppDatabase): VacancyDao {
        return db.vacancyDao()
    }

    @Provides
    fun provideVacancyFavouriteDbConverters(): VacancyFavouriteDbConverters {
        return VacancyFavouriteDbConverters()
    }
}