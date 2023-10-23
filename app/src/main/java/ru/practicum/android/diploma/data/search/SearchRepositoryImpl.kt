package ru.practicum.android.diploma.data.search

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.PagingInfo
import ru.practicum.android.diploma.data.network.dto.VacancyDto
import ru.practicum.android.diploma.data.network.dto.VacancyRequest
import ru.practicum.android.diploma.data.network.dto.VacancyResponse
import ru.practicum.android.diploma.domain.models.Filter
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.search.SearchRepository
import ru.practicum.android.diploma.util.Constants.PER_PAGE
import ru.practicum.android.diploma.util.NetworkResource
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val networkClient: NetworkClient
) : SearchRepository {

    override fun search(
        expression: String, page: Int, filter: Filter?
    ): Flow<Pair<NetworkResource<List<Vacancy>>, PagingInfo>> = flow {

        val options = VacancyRequest(HashMap())

        filter?.let {
            filter.countryId?.let { options.request["area"] = it }
            filter.regionId?.let { options.request["area"] = it }
            filter.industryId?.let { options.request["industry"] = it }
            filter.expectedSalary?.let { options.request["salary"] = it.toString() }
            options.request["only_with_salary"] = filter.isOnlyWithSalary.toString()
        }

        options.request["page"] = page.toString()
        options.request["per_page"] = PER_PAGE
        options.request["text"] = expression
        val response = networkClient.search(options)
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
                        page = response.page, pages = response.pages, found = response.found
                    )
                )
            }

            else -> {
                emit(NetworkResource<List<Vacancy>>(code = 400) to PagingInfo())
            }
        }
    }
}
