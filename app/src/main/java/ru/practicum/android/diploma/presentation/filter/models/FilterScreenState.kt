package ru.practicum.android.diploma.presentation.filter.models

sealed interface FilterScreenState{
    object Default: FilterScreenState
    data class Content(
        val countryName: String? = null,
        val regionName: String? = null,
        val industryName: String? = null,
        val expectedSalary: Long? = null,
        val isOnlyWithSalary: Boolean = false
    ): FilterScreenState
}