package ru.practicum.android.diploma.data.favourite

import ru.practicum.android.diploma.data.db.VacancyDao
import ru.practicum.android.diploma.data.db.converters.VacancyFavouriteDbConverters
import ru.practicum.android.diploma.domain.favourite.FavouriteRepository
import ru.practicum.android.diploma.domain.models.Vacancy
import javax.inject.Inject

class FavouriteRepositoryImpl @Inject constructor(
    private val vacancyDao: VacancyDao,
    private val converter: VacancyFavouriteDbConverters
): FavouriteRepository {
    override suspend fun getAllVacancies(): List<Vacancy> {
        return vacancyDao.getAllVacancies().map {
            converter.map(it)
        }
    }
}