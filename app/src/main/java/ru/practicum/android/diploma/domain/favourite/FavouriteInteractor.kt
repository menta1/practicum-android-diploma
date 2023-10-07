package ru.practicum.android.diploma.domain.favourite

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Vacancy

interface FavouriteInteractor {
    fun getAllVacancies(): Flow<List<Vacancy>>
}