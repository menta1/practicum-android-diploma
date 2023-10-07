package ru.practicum.android.diploma.domain.favourite

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Vacancy

interface FavouriteRepository {
    suspend fun saveVacancy(vacancy: Vacancy)

    suspend fun deleteVacancy(vacancy: Vacancy)

    suspend fun getAllVacancies(): Flow<List<Vacancy>>

    suspend fun getVacancyById(vacancyId: Int): Flow<Vacancy>
}