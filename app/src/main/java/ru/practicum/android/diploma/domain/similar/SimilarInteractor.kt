package ru.practicum.android.diploma.domain.similar

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.network.PagingInfo
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.util.NetworkResource

interface SimilarInteractor {
    fun getSimilarVacancy(
        vacancyId: String,
        page: Int
    ): Flow<Pair<NetworkResource<List<Vacancy>>, PagingInfo>>
}