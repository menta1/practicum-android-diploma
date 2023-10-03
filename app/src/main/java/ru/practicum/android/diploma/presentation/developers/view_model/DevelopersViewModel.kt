package ru.practicum.android.diploma.presentation.developers.view_model

import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.domain.developers.DevelopersInteractor
import javax.inject.Inject

class DevelopersViewModel @Inject constructor(val interactor: DevelopersInteractor): ViewModel() {

}