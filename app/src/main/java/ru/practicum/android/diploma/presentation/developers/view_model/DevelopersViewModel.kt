package ru.practicum.android.diploma.presentation.developers.view_model

import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.domain.share.SharingInteractor
import ru.practicum.android.diploma.domain.share.model.SharingData
import javax.inject.Inject

class DevelopersViewModel @Inject constructor(val interactor: SharingInteractor) : ViewModel() {

    fun openLinkGitHub(link: String) {
        interactor.openLink(SharingData(link))
    }
}