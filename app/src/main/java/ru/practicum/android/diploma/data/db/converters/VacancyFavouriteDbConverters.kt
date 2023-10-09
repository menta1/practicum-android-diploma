package ru.practicum.android.diploma.data.db.converters

import ru.practicum.android.diploma.data.db.VacancyEntity
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyDetail

class VacancyFavouriteDbConverters {
    fun map(entity: VacancyEntity): Vacancy {
        return Vacancy(
            id = entity.id.toString(),
            name = entity.name,
            city = entity.city,
            employer = entity.employer,
            employerLogoUrls = entity.employerLogoUrls,
            currency = entity.currency,
            salaryFrom = entity.salaryFrom,
            salaryTo = entity.salaryTo
        )
    }

    fun mapDetails(vacancy: VacancyEntity): VacancyDetail {
        return VacancyDetail(
            id = vacancy.id.toString(),
            name = vacancy.name,
            city = vacancy.city,
            employer = vacancy.employer,
            employerLogoUrls = "",
            currency = vacancy.currency,
            salaryFrom = vacancy.salaryFrom,
            salaryTo = vacancy.salaryTo,
            experience = vacancy.experience,
            employmentType = vacancy.employmentType,
            schedule = vacancy.schedule,
            description = vacancy.description,
            keySkills = listOf(vacancy.keySkills),
            phone = vacancy.phone,
            email = vacancy.email,
            contactPerson = vacancy.contactPerson
        )
    }

    fun mapDetails(vacancy: VacancyDetail): VacancyEntity {
        return VacancyEntity(
            id = vacancy.id.toInt(),
            name = vacancy.name,
            city = vacancy.city,
            employer = vacancy.employer,
            employerLogoUrls = vacancy.employerLogoUrls,
            currency = vacancy.currency,
            salaryFrom = vacancy.salaryFrom,
            salaryTo = vacancy.salaryTo,
            experience = vacancy.experience,
            employmentType = vacancy.employmentType,
            schedule = vacancy.schedule,
            description = vacancy.description,
            keySkills = vacancy.keySkills.toString(),
            phone = vacancy.phone,
            email = vacancy.email,
            contactPerson = vacancy.contactPerson
        )
    }
}