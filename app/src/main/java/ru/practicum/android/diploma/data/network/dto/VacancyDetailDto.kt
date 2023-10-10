package ru.practicum.android.diploma.data.network.dto

import ru.practicum.android.diploma.domain.models.VacancyDetail
import com.google.gson.annotations.SerializedName

data class VacancyDetailDto(
    @SerializedName("area") val area: Area,
    @SerializedName("contacts") val contacts: Contacts,
    @SerializedName("description") val description: String,
    @SerializedName("employer") val employer: Employer,
    @SerializedName("employment") val employment: Employment,
    @SerializedName("experience") val experience: Experience,
    @SerializedName("id") val id: String,
    @SerializedName("key_skills") val keySkills: List<KeySkill>,
    @SerializedName("name") val name: String,
    @SerializedName("salary") val salary: Salary,
    @SerializedName("schedule") val schedule: Schedule
) {

    data class Area(
        @SerializedName("name") val name: String
    )

    data class Contacts(
        @SerializedName("email") val email: String?,
        @SerializedName("name") val name: String?,
        @SerializedName("phones") val phones: List<Phone>?
    ) {
        data class Phone(
            @SerializedName("city") val city: String,
            @SerializedName("comment") val comment: String?,
            @SerializedName("country") val country: String,
            @SerializedName("number") val number: String
        )
    }

    data class Employer(
        @SerializedName("logo_urls") val logoUrls: LogoUrls?,
        @SerializedName("name") val name: String
    ) {
        data class LogoUrls(
            @SerializedName("original") val original: String?
        )
    }

    data class Employment(
        @SerializedName("name") val name: String
    )

    data class Experience(
        @SerializedName("name") val name: String
    )

    data class KeySkill(
        @SerializedName("name") val name: String
    )

    data class Salary(
        @SerializedName("currency") val currency: String?,
        @SerializedName("from") val from: Int?,
        @SerializedName("to") val to: Int?
    )

    data class Schedule(
        @SerializedName("name") val name: String
    )


    fun toVacancyDetail(): VacancyDetail = VacancyDetail(
        id = id,
        name = name,
        city = area.name,
        employer = employer.name,
        employerLogoUrls = employer.logoUrls?.original,
        currency = salary.currency,
        salaryFrom = salary.from,
        salaryTo = salary.to,
        experience = experience.name,
        employmentType = employment.name,
        schedule = schedule.name,
        description = description,
        keySkills = keySkills.map { KeySkill -> KeySkill.name },
        phone = contacts.phones?.map { Phone -> Phone.country + Phone.city + Phone.number },
        email = contacts.email,
        contactPerson = contacts.name
    )
}

