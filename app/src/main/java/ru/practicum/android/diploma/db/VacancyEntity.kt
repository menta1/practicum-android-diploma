package ru.practicum.android.diploma.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vacancy_table")
data class VacancyEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val city: String,
    val employer: String,
    val employerLogoUrls: String,
    val currency: String,
    val salaryFrom: Int?,
    val salaryTo: Int?,
    val experience: String,
    val employmentType: String,
    val schedule: String,
    val description: String,
    val keySkills: List<String>,
    val phone: String,
    val email: String,
    val contactPerson: String
)
