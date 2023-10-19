package ru.practicum.android.diploma.presentation.filter.models

sealed interface FilterRegionScreenState{
    data class Content(val isListEmpty: Boolean): FilterRegionScreenState
    object NoInternet: FilterRegionScreenState
    object UnableToGetResult: FilterRegionScreenState
    object Loading: FilterRegionScreenState
}