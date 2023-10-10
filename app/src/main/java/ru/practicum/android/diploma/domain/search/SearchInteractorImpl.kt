package ru.practicum.android.diploma.domain.search

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.network.PagingInfo
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.util.NetworkResource
import javax.inject.Inject

class SearchInteractorImpl @Inject constructor(
    private val repository: SearchRepository
) : SearchInteractor {
    override fun search(
        expression: String,
        page: Int
    ): Flow<Pair<NetworkResource<List<Vacancy>>, PagingInfo>> {
        return repository.search(expression, page)
    }
}