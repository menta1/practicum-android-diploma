package ru.practicum.android.diploma.data.db.converters

import ru.practicum.android.diploma.data.db.PhoneDto
import ru.practicum.android.diploma.data.db.VacancyEntity
import ru.practicum.android.diploma.domain.models.Phone
import ru.practicum.android.diploma.domain.models.VacancyDetail

class VacancyDetailDbConverters {
    fun map(vacancy: VacancyEntity): VacancyDetail {
        return VacancyDetail(
            id = vacancy.id.toString(),
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
            phone = vacancy.phone?.map {
                mapPhoneToDomain(it)
            },
            email = vacancy.email,
            contactPerson = vacancy.contactPerson,
            url = vacancy.url
        )
    }

    fun map(vacancy: VacancyDetail): VacancyEntity {
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
            keySkills = vacancy.keySkills,
            phone = vacancy.phone?.map {
                mapPhoneToData(it)
            },
            email = vacancy.email,
            contactPerson = vacancy.contactPerson,
            url = vacancy.url
        )
    }

    private fun mapPhoneToDomain(phoneDto: PhoneDto): Phone {
        return Phone(
            number = phoneDto.number,
            comment = phoneDto.comment
        )
    }

    private fun mapPhoneToData(phone: Phone): PhoneDto {
        return PhoneDto(
            number = phone.number,
            comment = phone.comment
        )
    }
}