package ru.practicum.android.diploma.domain.developers

import javax.inject.Inject

class DevelopersInteractorImpl @Inject constructor(
    private val repository: DevelopersRepository
): DevelopersInteractor {
}