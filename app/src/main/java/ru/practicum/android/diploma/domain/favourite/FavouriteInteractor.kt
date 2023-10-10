package ru.practicum.android.diploma.domain.favourite

import ru.practicum.android.diploma.domain.models.Vacancy

interface FavouriteInteractor {
    suspend fun getAllVacancies(): List<Vacancy>
}