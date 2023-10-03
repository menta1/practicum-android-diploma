package ru.practicum.android.diploma.domain.favourite

import javax.inject.Inject

class FavouriteInteractorImpl @Inject constructor(
    private val repository: FavouriteRepository
) : FavouriteInteractor {
}