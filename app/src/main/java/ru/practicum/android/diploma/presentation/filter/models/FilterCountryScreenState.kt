package ru.practicum.android.diploma.presentation.filter.models

import ru.practicum.android.diploma.domain.models.Region

sealed interface FilterCountryScreenState{
    data class Content(val countries: List<Region>): FilterCountryScreenState
    object NoInternet: FilterCountryScreenState
    object UnableToGetResult: FilterCountryScreenState
    object Loading: FilterCountryScreenState
}