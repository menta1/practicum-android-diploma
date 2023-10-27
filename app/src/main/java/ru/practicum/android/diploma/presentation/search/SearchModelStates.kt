package ru.practicum.android.diploma.presentation.search

import ru.practicum.android.diploma.domain.models.Vacancy

sealed interface SearchModelStates {
    data class Content(
        val data: List<Vacancy>,
        val foundUsers: String
    ): SearchModelStates
    data class NoSearch(
        val isHasFilters: Boolean
    ) : SearchModelStates
    object Search : SearchModelStates
    object NewSearch: SearchModelStates
    object Loading : SearchModelStates
    object NoInternet : SearchModelStates
    object FailedToGetList : SearchModelStates
    object ServerError: SearchModelStates
}