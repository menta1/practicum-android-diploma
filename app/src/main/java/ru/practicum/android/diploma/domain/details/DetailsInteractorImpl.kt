package ru.practicum.android.diploma.domain.details

import javax.inject.Inject

class DetailsInteractorImpl @Inject constructor(
    private val repository: DetailsRepository
) : DetailsInteractor {
}