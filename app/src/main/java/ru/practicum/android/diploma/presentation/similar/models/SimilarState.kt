package ru.practicum.android.diploma.presentation.similar.models

import ru.practicum.android.diploma.domain.models.Vacancy

sealed interface SimilarState {
    object Loading: SimilarState
    data class Content(
        val data: List<Vacancy>
    ): SimilarState
    object Error: SimilarState
    object NoInternet: SimilarState
}