package ru.practicum.android.diploma.presentation.filter.models

sealed interface FilterScreenState{

    data class Default(val isClearButtonPressed: Boolean = false): FilterScreenState
    data class Content(
        val countryName: String? = null,
        val regionName: String? = null,
        val industryName: String? = null,
        val expectedSalary: Long? = null,
        val isOnlyWithSalary: Boolean = false,
        val isClearButtonPressed: Boolean = false
    ): FilterScreenState
    data class SalaryInput(val isInputNotEmpty: Boolean): FilterScreenState

}