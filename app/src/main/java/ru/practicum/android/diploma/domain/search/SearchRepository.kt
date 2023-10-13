package ru.practicum.android.diploma.domain.search

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.network.PagingInfo
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.util.NetworkResource

interface SearchRepository {
    fun search(
        expression: String,
        page: Int
    ): Flow<Pair<NetworkResource<List<Vacancy>>, PagingInfo>>
}