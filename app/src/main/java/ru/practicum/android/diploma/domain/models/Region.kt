package ru.practicum.android.diploma.domain.models

data class Region(
    val id: String,
    val parentId: String?,
    val name: String,
    val areas: List<Region>
)
