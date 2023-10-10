package ru.practicum.android.diploma.domain.details

import ru.practicum.android.diploma.domain.models.VacancyDetail
import ru.practicum.android.diploma.util.NetworkResource

interface DetailsRepository {
    suspend fun saveVacancy(vacancy: VacancyDetail)

    suspend fun deleteVacancy(vacancy: VacancyDetail)

    suspend fun getFavouriteVacancy(vacancyId: Int): VacancyDetail

    suspend fun isVacancyInFavourites(vacancyId: Int): Boolean

    suspend fun getVacancyDetails(vacancyId: Int): NetworkResource<VacancyDetail>
}