package ru.practicum.android.diploma.data.db.converters

import ru.practicum.android.diploma.data.db.VacancyEntity
import ru.practicum.android.diploma.domain.models.VacancyDetail

class VacancyDetailDbConverters {
    fun map(vacancy: VacancyEntity): VacancyDetail {
        return VacancyDetail(
            id = vacancy.id,
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
            keySkills = vacancy.keySkills,
            phone = vacancy.phone,
            email = vacancy.email,
            contactPerson = vacancy.contactPerson
        )
    }

    fun map(vacancy: VacancyDetail): VacancyEntity {
        return VacancyEntity(
            id = vacancy.id,
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
            keySkills = vacancy.keySkills,
            phone = vacancy.phone,
            email = vacancy.email,
            contactPerson = vacancy.contactPerson
        )
    }
}