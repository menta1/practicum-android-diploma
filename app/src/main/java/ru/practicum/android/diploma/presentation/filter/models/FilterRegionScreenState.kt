package ru.practicum.android.diploma.presentation.filter.models

import ru.practicum.android.diploma.domain.models.Region

sealed interface FilterRegionScreenState{
    data class Content(val regions: List<Region>): FilterRegionScreenState
    object NoInternet: FilterRegionScreenState
    object UnableToGetResult: FilterRegionScreenState
    object Loading: FilterRegionScreenState
    object EscapeScreen: FilterRegionScreenState
}