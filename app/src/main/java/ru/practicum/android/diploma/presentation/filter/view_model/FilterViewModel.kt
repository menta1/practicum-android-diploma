package ru.practicum.android.diploma.presentation.filter.view_model

import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.domain.filter.FilterInteractor
import javax.inject.Inject

class FilterViewModel @Inject constructor(private val interactor: FilterInteractor) : ViewModel() {
}