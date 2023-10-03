package ru.practicum.android.diploma.domain.similar

import javax.inject.Inject

class SimilarInteractorImpl @Inject constructor(
    private val repository: SimilarRepository
) : SimilarInteractor {
}