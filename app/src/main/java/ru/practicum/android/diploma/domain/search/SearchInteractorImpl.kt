package ru.practicum.android.diploma.domain.search

import javax.inject.Inject

class SearchInteractorImpl @Inject constructor(
    private val repository: SearchRepository
) : SearchInteractor {
}