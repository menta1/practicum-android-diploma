package ru.practicum.android.diploma.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.practicum.android.diploma.data.db.converters.ConverterType

@Database(
    version = 2,
    entities = [VacancyEntity::class],
    exportSchema = false
)
@TypeConverters(ConverterType::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun vacancyDao(): VacancyDao
}