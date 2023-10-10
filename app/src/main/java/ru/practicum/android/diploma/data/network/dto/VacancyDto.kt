package ru.practicum.android.diploma.data.network.dto

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.domain.models.Vacancy

data class VacancyDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("area") val area: Area,
    @SerializedName("employer") val employer: Employer,
    @SerializedName("salary") val salary: Salary?,
    @SerializedName("page") val page: Int?,
    @SerializedName("pages") val pages: Int?,
    @SerializedName("found") val found: Int?
) {
    data class Salary(
        @SerializedName("currency") val currency: String?,
        @SerializedName("from") val from: Int,
        @SerializedName("to") val to: Int
    )

    data class Area(
        @SerializedName("name") val name: String
    )

    data class Employer(
        @SerializedName("name") val name: String,
        @SerializedName("logo_urls") val url: LogoUrls?
    ) {
        data class LogoUrls(
            @SerializedName("original") val logo: String
        )
    }

    fun toVacancy(): Vacancy = Vacancy(
        id = id,
        name = name,
        city = area.name,
        employer = employer.name,
        employerLogoUrls = employer.url?.logo,
        currency = salary?.currency,
        salaryFrom = salary?.from,
        salaryTo = salary?.to,

    )
}
