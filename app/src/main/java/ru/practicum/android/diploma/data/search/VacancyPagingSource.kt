package ru.practicum.android.diploma.data.search

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.dto.VacancyDto
import ru.practicum.android.diploma.data.network.dto.VacancyRequest
import ru.practicum.android.diploma.data.network.dto.VacancyResponse
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.util.Resource

class VacancyPagingSource(
    private val pageSize: Int,
    private val expression: String,
    private val networkClient: NetworkClient
) : PagingSource<Int, Vacancy>() {

    override fun getRefreshKey(state: PagingState<Int, Vacancy>): Int? {
        val position = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(position) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Vacancy> {
        if (expression.isBlank()) {
            return LoadResult.Page(emptyList(), prevKey = null, nextKey = null)
        }
        val page = params.key ?: 0
        val maxPageSize = params.loadSize.coerceAtMost(pageSize)

        val vacancies = getVacancy(
            VacancyRequest(
                expression = expression,
                page = page,
                pageSize = pageSize
            )
        )
        return if (vacancies is Resource.Success) {
            LoadResult.Page(
                data = vacancies.data!!,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (vacancies.data.size < maxPageSize) null else page + 1
            )
        } else {
            LoadResult.Error(throwable = IllegalStateException(vacancies.message))
        }
    }

    private suspend fun getVacancy(
        vacancyRequest: VacancyRequest
    ): Resource<List<Vacancy>> = withContext(Dispatchers.IO) {

        val response = networkClient.search(vacancyRequest)
        return@withContext when (response.resultCode) {
            -1 -> {
                Resource.Error("Проверьте подключение к интернету")
            }

            200 -> {
                Resource.Success(
                    (response as VacancyResponse).results.map(
                        VacancyDto::toVacancy
                    )
                )
            }

            else -> {
                Resource.Error("Ошибка сервера")
            }
        }
    }
}