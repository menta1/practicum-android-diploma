package ru.practicum.android.diploma.data.db.converters

import ru.practicum.android.diploma.data.db.VacancyEntity
import ru.practicum.android.diploma.domain.models.Vacancy

class VacancyFavouriteDbConverters {
    fun map(entity: VacancyEntity): Vacancy {
        return Vacancy(
            id = entity.id.toString(),
            name = entity.name,
            city = entity.city,
            employer = entity.employer,
            currency = entity.currency,
            salaryFrom = entity.salaryFrom,
            salaryTo = entity.salaryTo
        )
    }
}