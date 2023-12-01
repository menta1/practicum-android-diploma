package ru.practicum.android.diploma.data.search

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.Constants.NO_INTERNET
import ru.practicum.android.diploma.data.Constants.OK_RESPONSE
import ru.practicum.android.diploma.data.Constants.PAGE
import ru.practicum.android.diploma.data.Constants.PER_PAGE
import ru.practicum.android.diploma.data.Constants.PER_PAGE_ITEMS
import ru.practicum.android.diploma.data.Constants.SERVER_ERROR
import ru.practicum.android.diploma.data.Constants.TEXT
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.PagingInfo
import ru.practicum.android.diploma.data.network.dto.VacancyDto
import ru.practicum.android.diploma.data.network.dto.VacancyRequest
import ru.practicum.android.diploma.data.network.dto.VacancyResponse
import ru.practicum.android.diploma.domain.models.Filter
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.search.SearchRepository
import ru.practicum.android.diploma.util.NetworkResource
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val networkClient: NetworkClient
) : SearchRepository {

    override fun search(
        expression: String, page: Int, filter: Filter?
    ): Flow<Pair<NetworkResource<List<Vacancy>>, PagingInfo>> = flow {

        val options = HashMap<String, String>()
        filter?.let {
            filter.countryId?.let { options.put(AREA, it) }
            filter.regionId?.let { options.put(AREA, it) }
            filter.industryId?.let { options.put(INDUSTRY, it) }
            filter.expectedSalary?.let { options.put(SALARY, it.toString()) }
            options.put(ONLY_WITH_SALARY, filter.isOnlyWithSalary.toString())
        }
        options[PAGE] = page.toString()
        options[PER_PAGE] = PER_PAGE_ITEMS
        options[TEXT] = expression

        val response = networkClient.search(VacancyRequest(options))
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

    companion object {
        const val AREA = "area"
        const val INDUSTRY = "industry"
        const val SALARY = "salary"
        const val ONLY_WITH_SALARY = "only_with_salary"
    }
}
