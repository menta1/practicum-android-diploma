package ru.practicum.android.diploma.domain.similar

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.network.PagingInfo
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.util.NetworkResource
import javax.inject.Inject

class SimilarInteractorImpl @Inject constructor(
    private val repository: SimilarRepository
) : SimilarInteractor {
    override fun getSimilarVacancy(
        vacancyId: String,
        page: Int
    ): Flow<Pair<NetworkResource<List<Vacancy>>, PagingInfo>> {
        return repository.getSimilarVacancy(vacancyId, page)
    }
}