package ru.practicum.android.diploma.domain.favourite

import ru.practicum.android.diploma.domain.models.Vacancy
import javax.inject.Inject

class FavouriteInteractorImpl @Inject constructor(
    private val repository: FavouriteRepository
) : FavouriteInteractor {
    override suspend fun getAllVacancies(): List<Vacancy> {
        return repository.getAllVacancies()
    }
}