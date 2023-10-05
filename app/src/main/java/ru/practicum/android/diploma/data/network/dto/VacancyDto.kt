package ru.practicum.android.diploma.data.network.dto

import ru.practicum.android.diploma.domain.models.Vacancy

data class VacancyDto(
    val id: String,
    val name: String,
    val city: String,
    val employer: String,
    val employerLogoUrls: String,
    val currency: String,
    val salaryFrom: Int?,
    val salaryTo: Int?
) {
    fun toVacancy(): Vacancy = Vacancy(
        id = id,
        name = name,
        city = city,
        employer = employer,
        employerLogoUrls = employerLogoUrls,
        currency = currency,
        salaryFrom = salaryFrom,
        salaryTo = salaryTo
    )
}
