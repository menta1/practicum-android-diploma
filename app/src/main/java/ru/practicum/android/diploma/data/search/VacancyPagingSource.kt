package ru.practicum.android.diploma.data.search

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.CoroutineDispatcher
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
    private val ioDispatcher: CoroutineDispatcher,
    private val networkClient: NetworkClient
) : PagingSource<Int, Vacancy>() {

    override fun getRefreshKey(state: PagingState<Int, Vacancy>): Int? {
        val position = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(position) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Vacancy> {
        val pageIndex = params.key ?: 0

        val vacancies = getVacancy(pageSize, pageIndex, expression)
        return try {
            return if (vacancies is Resource.Success) {
                LoadResult.Page(
                    data = vacancies.data!!,
                    prevKey = if (pageIndex == 0) null else pageIndex - 1,
                    nextKey = if (vacancies.data.size == params.loadSize) pageIndex + (params.loadSize / pageSize) else null
                )
            } else {
                LoadResult.Error(throwable = IllegalStateException(vacancies.message))
            }
        } catch (e: Exception) {
            println(e.stackTrace)
            LoadResult.Error(
                throwable = e
            )
        }

    }

    private suspend fun getVacancy(
        pageIndex: Int,
        perPage: Int,
        expression: String
    ): Resource<List<Vacancy>> = withContext(ioDispatcher) {
        val page = pageIndex * perPage
        val response = networkClient.search(VacancyRequest(perPage, page, expression))
        return@withContext when (response.resultCode) {
            -1 -> {
                Resource.Error("Проверьте подключение к интернету")
            }

            200 -> {
                try {
                    Resource.Success(
                        (response as VacancyResponse).results.map(
                            VacancyDto::toVacancy
                        )
                    )
                } catch (e: Exception){
                    Log.d("tag", "response " + (response as VacancyResponse).results )
                    Log.d("tag", "e.stackTrace " + e.stackTrace )
                    Resource.Error("Ошибка сервера")
                }

            }

            else -> {
                Resource.Error("Ошибка сервера")
            }
        }
    }
}