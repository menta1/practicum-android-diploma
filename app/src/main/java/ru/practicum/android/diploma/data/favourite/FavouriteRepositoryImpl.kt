package ru.practicum.android.diploma.data.favourite

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.db.AppDatabase
import ru.practicum.android.diploma.data.db.converters.VacancyFavouriteDbConverters
import ru.practicum.android.diploma.domain.favourite.FavouriteRepository
import ru.practicum.android.diploma.domain.models.Vacancy
import javax.inject.Inject

class FavouriteRepositoryImpl @Inject constructor(
    private val database: AppDatabase,
    private val converter: VacancyFavouriteDbConverters
): FavouriteRepository {
    override suspend fun saveVacancy(vacancy: Vacancy) {
        database.vacancyDao().insertVacancy(converter.map(vacancy))
    }

    override suspend fun deleteVacancy(vacancy: Vacancy) {
        database.vacancyDao().deleteVacancy(converter.map(vacancy))
    }

    override suspend fun getAllVacancies(): Flow<List<Vacancy>> = flow {
        val vacancies = database.vacancyDao().getAllVacancies().map {
            converter.map(it)
        }
        emit(vacancies)
    }

    override suspend fun getVacancyById(vacancyId: Int): Flow<Vacancy> = flow {
        val vacancy = database.vacancyDao().getVacancyById(vacancyId)
        emit(converter.map(vacancy))
    }
}