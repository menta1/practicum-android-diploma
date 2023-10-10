package ru.practicum.android.diploma.data.details

import ru.practicum.android.diploma.data.db.VacancyDao
import ru.practicum.android.diploma.data.db.converters.VacancyDetailDbConverters
import ru.practicum.android.diploma.data.db.converters.VacancyFavouriteDbConverters
import ru.practicum.android.diploma.domain.details.DetailsRepository
import ru.practicum.android.diploma.domain.models.VacancyDetail
import javax.inject.Inject

class DetailsRepositoryImpl @Inject constructor(
    private val vacancyDao: VacancyDao,
    private val converter: VacancyDetailDbConverters
): DetailsRepository {

    override suspend fun saveVacancy(vacancy: VacancyDetail) {
        vacancyDao.insertVacancy(converter.map(vacancy))
    }

    override suspend fun deleteVacancy(vacancy: VacancyDetail) {
        vacancyDao.deleteVacancy(converter.map(vacancy))
    }

    override suspend fun getVacancyById(vacancyId: Int): VacancyDetail {
        return converter.map(vacancyDao.getVacancyById(vacancyId))
    }

    override suspend fun isVacancyInFavourites(vacancyId: Int): Boolean {
        return vacancyDao.isVacancyInFavourites(vacancyId)
    }
}