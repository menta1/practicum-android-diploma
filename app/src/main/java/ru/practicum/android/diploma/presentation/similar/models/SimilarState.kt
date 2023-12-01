package ru.practicum.android.diploma.presentation.similar.models

import ru.practicum.android.diploma.domain.models.Vacancy

sealed interface SimilarState {
    data object Loading: SimilarState
    data class Content(
        val data: List<Vacancy>
    ): SimilarState
    data object Empty: SimilarState
    data class Error(
        val error: ErrorSimilarScreen
    ): SimilarState
}