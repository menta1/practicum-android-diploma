package ru.practicum.android.diploma.domain.favourite

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.domain.models.Vacancy
import javax.inject.Inject

class FavouriteInteractorImpl @Inject constructor(
    private val repository: FavouriteRepository
) : FavouriteInteractor {
    override fun getAllVacancies(): Flow<List<Vacancy>> = flow {
//        emit(repository.getAllVacancies())
    }
}