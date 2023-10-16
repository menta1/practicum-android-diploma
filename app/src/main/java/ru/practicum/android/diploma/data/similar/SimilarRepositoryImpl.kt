package ru.practicum.android.diploma.data.similar

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.PagingInfo
import ru.practicum.android.diploma.data.network.dto.VacancyDto
import ru.practicum.android.diploma.data.network.dto.VacancyResponse
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.similar.SimilarRepository
import ru.practicum.android.diploma.util.Constants.PER_PAGE
import ru.practicum.android.diploma.util.NetworkResource
import javax.inject.Inject

class SimilarRepositoryImpl @Inject constructor(
    private val networkClient: NetworkClient
) : SimilarRepository {
    private val options: HashMap<String, String> = HashMap()
    override fun getSimilarVacancy(
        vacancyId: String,
        page: Int
    ): Flow<Pair<NetworkResource<List<Vacancy>>, PagingInfo>> = flow {
        options["page"] = page.toString()
        options["per_page"] = PER_PAGE
        val response = networkClient.getSimilarVacancy(vacancyId, options)
        when (response.resultCode) {
            -1 -> {
                emit(NetworkResource<List<Vacancy>>(code = -1) to PagingInfo())
            }

            200 -> {
                emit(
                    NetworkResource(
                        (response as VacancyResponse).results.map(
                            VacancyDto::toVacancy
                        ), 200
                    ) to PagingInfo(
                        page = response.page,
                        pages = response.pages,
                        found = response.found
                    )
                )
            }

            else -> {
                emit(NetworkResource<List<Vacancy>>(code = 400) to PagingInfo())
            }
        }
    }
}