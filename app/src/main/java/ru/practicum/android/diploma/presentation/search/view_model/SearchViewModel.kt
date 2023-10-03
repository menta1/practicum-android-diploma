package ru.practicum.android.diploma.presentation.search.view_model

import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.domain.search.SearchInteractor
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    interactor: SearchInteractor
): ViewModel() {
}