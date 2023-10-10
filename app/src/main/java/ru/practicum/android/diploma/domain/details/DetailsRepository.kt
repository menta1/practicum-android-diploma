package ru.practicum.android.diploma.domain.details

import ru.practicum.android.diploma.domain.models.VacancyDetail

interface DetailsRepository {
    suspend fun saveVacancy(vacancy: VacancyDetail)

    suspend fun deleteVacancy(vacancy: VacancyDetail)

    suspend fun getVacancyById(vacancyId: Int): VacancyDetail

    suspend fun isVacancyInFavourites(vacancyId: Int): Boolean
}