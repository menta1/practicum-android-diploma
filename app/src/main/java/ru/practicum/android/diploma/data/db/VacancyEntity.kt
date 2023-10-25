package ru.practicum.android.diploma.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

const val VACANCY_DATABASE = "vacancy_table"

@Entity(tableName = VACANCY_DATABASE)
data class VacancyEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val city: String,
    val employer: String,
    val employerLogoUrls: String?,
    val currency: String?,
    val salaryFrom: Int?,
    val salaryTo: Int?,
    val experience: String,
    val employmentType: String,
    val schedule: String,
    val description: String,
    val keySkills: List<String>,
    val phone: List<PhoneDto>?,
    val email: String?,
    val contactPerson: String?,
    val url: String
)
