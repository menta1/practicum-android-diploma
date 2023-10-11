package ru.practicum.android.diploma.domain.models

data class Filter(

    val countryName: String? = null,
    val regionName: String? = null,
    val industryName: String? = null,
    val regionId: String? = null,
    val industryId: String? = null,
    val expectedSalary: Int? = null,
    val isOnlyWithSalary: Boolean = false
)
