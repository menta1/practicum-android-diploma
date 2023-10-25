package ru.practicum.android.diploma.presentation.search

sealed class SearchModelState {
    object NoSearch : SearchModelState()
    object Search : SearchModelState()
    object Loading : SearchModelState()
    object Loaded : SearchModelState()
    object NoInternet : SearchModelState()
    object FailedToGetList : SearchModelState()
    object ServerError : SearchModelState()
}