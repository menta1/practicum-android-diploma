package ru.practicum.android.diploma.domain.models

data class Vacancy(
    val id: String,
    val name: String,
    val city: String,
    //Работодатель
    val employer: String,
    //Зарплата
    val currency: String,
    val salaryFrom: Int,
    val salaryTo: Int?,
)
