package ru.practicum.android.diploma.domain.details

import ru.practicum.android.diploma.domain.models.VacancyDetail
import ru.practicum.android.diploma.util.NetworkResource

interface DetailsInteractor {

    suspend fun saveVacancy(vacancy: VacancyDetail)

    suspend fun deleteVacancy(vacancy: VacancyDetail)

    suspend fun getFavouriteVacancy(vacancyId: String): VacancyDetail

    suspend fun isVacancyInFavourites(vacancyId: String): Boolean

    suspend fun getVacancyDetails(vacancyId: String): NetworkResource<VacancyDetail>
}