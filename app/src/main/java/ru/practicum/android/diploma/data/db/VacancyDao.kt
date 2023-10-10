package ru.practicum.android.diploma.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface VacancyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVacancy(vacancy: VacancyEntity)

    @Delete
    fun deleteVacancy(vacancy: VacancyEntity)

    @Query("SELECT * FROM vacancy_table")
    fun getAllVacancies(): List<VacancyEntity>

    @Query("SELECT * FROM vacancy_table WHERE id = :vacancyId")
    fun getVacancyById(vacancyId: String): VacancyEntity

    @Query("SELECT EXISTS(SELECT 1 FROM vacancy_table WHERE id = :vacancyId)")
    suspend fun isVacancyInFavourites(vacancyId: String): Boolean
}