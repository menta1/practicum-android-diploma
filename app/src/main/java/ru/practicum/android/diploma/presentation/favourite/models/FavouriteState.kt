package ru.practicum.android.diploma.presentation.favourite.models

import ru.practicum.android.diploma.domain.models.Vacancy

sealed class FavouriteState {
    object Loading: FavouriteState()
    data class Content(
        val data: List<Vacancy>
    ): FavouriteState()
    object Error: FavouriteState()
    object Empty: FavouriteState()
}