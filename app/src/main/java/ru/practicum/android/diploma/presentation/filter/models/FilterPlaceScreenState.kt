package ru.practicum.android.diploma.presentation.filter.models

sealed interface FilterPlaceScreenState{
    object Default: FilterPlaceScreenState
    data class Content(
        val countryName: String? = null,
        val regionName: String? = null,
        val industryName: String? = null,
        val expectedSalary: Long? = null,
        val isOnlyWithSalary: Boolean = false
    ): FilterPlaceScreenState
    object EscapeScreen: FilterPlaceScreenState
}