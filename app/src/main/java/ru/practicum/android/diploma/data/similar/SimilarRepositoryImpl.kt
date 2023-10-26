package ru.practicum.android.diploma.data.similar

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.Constants.NO_INTERNET
import ru.practicum.android.diploma.data.Constants.OK_RESPONSE
import ru.practicum.android.diploma.data.Constants.PAGE
import ru.practicum.android.diploma.data.Constants.PER_PAGE
import ru.practicum.android.diploma.data.Constants.PER_PAGE_ITEMS
import ru.practicum.android.diploma.data.Constants.SERVER_ERROR
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.PagingInfo
import ru.practicum.android.diploma.data.network.dto.VacancyDto
import ru.practicum.android.diploma.data.network.dto.VacancyRequest
import ru.practicum.android.diploma.data.network.dto.VacancyResponse
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.similar.SimilarRepository
import ru.practicum.android.diploma.util.NetworkResource
import javax.inject.Inject

class SimilarRepositoryImpl @Inject constructor(
    private val networkClient: NetworkClient
) : SimilarRepository {

    private val options = HashMap<String, String>()
    override fun getSimilarVacancy(
        vacancyId: String, page: Int
    ): Flow<Pair<NetworkResource<List<Vacancy>>, PagingInfo>> = flow {
        options[PAGE] = page.toString()
        options[PER_PAGE] = PER_PAGE_ITEMS
        val response = networkClient.getSimilarVacancy(vacancyId, VacancyRequest(options))
        when (response.resultCode) {
            NO_INTERNET -> {
                emit(NetworkResource<List<Vacancy>>(code = NO_INTERNET) to PagingInfo())
            }

            OK_RESPONSE -> {
                emit(
                    NetworkResource(
                        (response as VacancyResponse).results.map(
                            VacancyDto::toVacancy
                        ), OK_RESPONSE
                    ) to PagingInfo(
                        page = response.page, pages = response.pages, found = response.found
                    )
                )
            }

            else -> {
                emit(NetworkResource<List<Vacancy>>(code = SERVER_ERROR) to PagingInfo())
            }
        }
    }
}