package ru.practicum.android.diploma.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    version = 1,
    entities = [VacancyEntity::class]
)
abstract class AppDatabase: RoomDatabase() {

    abstract fun vacancyDao(): VacancyDao
}