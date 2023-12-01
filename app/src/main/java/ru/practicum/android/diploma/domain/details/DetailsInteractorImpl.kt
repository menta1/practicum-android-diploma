package ru.practicum.android.diploma.domain.details

import ru.practicum.android.diploma.domain.models.VacancyDetail
import ru.practicum.android.diploma.util.NetworkResource
import javax.inject.Inject

class DetailsInteractorImpl @Inject constructor(
    private val repository: DetailsRepository,
) : DetailsInteractor {
    override suspend fun saveVacancy(vacancy: VacancyDetail) {
        repository.saveVacancy(vacancy)
    }

    override suspend fun deleteVacancy(vacancy: VacancyDetail) {
        repository.deleteVacancy(vacancy)
    }

    override suspend fun getFavouriteVacancy(vacancyId: String): VacancyDetail {
        return repository.getFavouriteVacancy(vacancyId)
    }

    override suspend fun isVacancyInFavourites(vacancyId: String): Boolean {
        return repository.isVacancyInFavourites(vacancyId)
    }

    override suspend fun getVacancyDetails(vacancyId: String): NetworkResource<VacancyDetail> {
        return repository.getVacancyDetails(vacancyId)
    }
}