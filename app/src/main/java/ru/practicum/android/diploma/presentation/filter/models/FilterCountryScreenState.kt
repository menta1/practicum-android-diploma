package ru.practicum.android.diploma.presentation.filter.models

sealed interface FilterCountryScreenState{
    object Content: FilterCountryScreenState
    object NoInternet: FilterCountryScreenState
    object UnableToGetResult: FilterCountryScreenState
    object Loading: FilterCountryScreenState
}