package ru.practicum.android.diploma.data.search

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.search.SearchRepository
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val networkClient: NetworkClient
) : SearchRepository {
    override fun search(expression: String): Flow<PagingData<Vacancy>> {
        return Pager(config = PagingConfig(
            pageSize = PAGE_SIZE, enablePlaceholders = false
        ), pagingSourceFactory = {
            VacancyPagingSource(
                pageSize = PAGE_SIZE, expression = expression, networkClient = networkClient
            )
        }).flow
    }

    private companion object {
        const val PAGE_SIZE = 20
    }
}
