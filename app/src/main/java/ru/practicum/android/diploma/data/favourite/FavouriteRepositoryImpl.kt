package ru.practicum.android.diploma.data.favourite

import kotlinx.coroutines.flow.first
import ru.practicum.android.diploma.data.db.AppDatabase
import ru.practicum.android.diploma.data.db.converters.VacancyFavouriteDbConverters
import ru.practicum.android.diploma.domain.favourite.FavouriteRepository
import ru.practicum.android.diploma.domain.models.Vacancy
import javax.inject.Inject

class FavouriteRepositoryImpl @Inject constructor(
    private val database: AppDatabase,
    private val converter: VacancyFavouriteDbConverters
): FavouriteRepository {
    override suspend fun getAllVacancies(): List<Vacancy> {
        val vacancies = database.vacancyDao().getAllVacancies()
        return vacancies.first().map {
            converter.map(it)
        }
    }
}