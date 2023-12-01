package ru.practicum.android.diploma.presentation.search

import ru.practicum.android.diploma.domain.models.Vacancy

sealed class SearchModelState {
    data class NoSearch(val isFilterEmpty: Boolean) : SearchModelState()
    object Search : SearchModelState()
    object Loading : SearchModelState()
    data class Loaded(val isFilterEmpty: Boolean)  : SearchModelState()
    object NoInternet : SearchModelState()
    object FailedToGetList : SearchModelState()
    data class Content(
        val vacancies: List<Vacancy>,
        val vacanciesNumber: String,
        val isFirstLaunch: Boolean
    ) : SearchModelState()
    data class NextPageLoading(val isLoading: Boolean): SearchModelState()
    object NoInternetWhilePaging: SearchModelState()
    object ServerErrorWhilePaging: SearchModelState()
}