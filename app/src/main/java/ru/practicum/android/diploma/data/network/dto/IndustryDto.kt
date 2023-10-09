package ru.practicum.android.diploma.data.network.dto

data class IndustryDto(
    val id: String,
    val name: String,
    val industries: List<IndustryDto>?
)
