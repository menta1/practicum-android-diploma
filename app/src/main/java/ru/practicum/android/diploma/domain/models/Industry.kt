package ru.practicum.android.diploma.domain.models

data class Industry(
    val id: String,
    val name: String,
    val industries: List<Industry>?,
    var isChecked: Boolean = false
)
