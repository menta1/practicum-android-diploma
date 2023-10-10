package ru.practicum.android.diploma.presentation.details.models

import ru.practicum.android.diploma.domain.models.VacancyDetail

sealed class DetailsState {
    object Loading: DetailsState()
    object Error: DetailsState()
    data class Content(
        val data: VacancyDetail
    ): DetailsState()
}