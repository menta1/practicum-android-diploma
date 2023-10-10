package ru.practicum.android.diploma.domain.search

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.util.Resource
import javax.inject.Inject

class SearchInteractorImpl @Inject constructor(
    private val repository: SearchRepository
) : SearchInteractor {
    override fun search(expression: String): Flow<PagingData<Vacancy>> {
        return repository.search(expression)
    }
}