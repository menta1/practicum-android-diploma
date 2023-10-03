package ru.practicum.android.diploma.presentation.similar.view_model

import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.domain.similar.SimilarInteractor
import javax.inject.Inject

class SimilarViewModel @Inject constructor(
    private val interactor: SimilarInteractor
): ViewModel() {
}