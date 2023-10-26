package ru.practicum.android.diploma.presentation.filter.models

import ru.practicum.android.diploma.domain.models.Industry

sealed interface FilterIndustryScreenState {
    data class Content(
        val industries: List<Industry>,
        val isSelected: Boolean
    ) : FilterIndustryScreenState

    object NoInternet : FilterIndustryScreenState
    object UnableToGetResult : FilterIndustryScreenState
    object Loading : FilterIndustryScreenState
}