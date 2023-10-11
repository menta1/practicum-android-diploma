package ru.practicum.android.diploma.presentation.details.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.details.DetailsInteractor
import ru.practicum.android.diploma.domain.models.VacancyDetail
import javax.inject.Inject

class DetailsViewModel @Inject constructor(val interactor: DetailsInteractor): ViewModel() {

    private val _vacancyDetail = MutableLiveData<VacancyDetail>()
    val vacancyDetail: LiveData<VacancyDetail> = _vacancyDetail

    private val _isFavouriteVacancy = MutableLiveData<Boolean>()
    val isFavouriteVacancy: LiveData<Boolean> = _isFavouriteVacancy

    fun saveVacancy(vacancy: VacancyDetail) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.saveVacancy(vacancy)
        }
    }

    fun deleteVacancy(vacancy: VacancyDetail) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.deleteVacancy(vacancy)
        }
    }

    fun getVacancyDetails(vacancyId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val isFavourite = interactor.isVacancyInFavourites(vacancyId)
            _isFavouriteVacancy.postValue(isFavourite)
            if (isFavourite) {
                _vacancyDetail.postValue(interactor.getFavouriteVacancy(vacancyId))
            } else {
                val networkResource = interactor.getVacancyDetails(vacancyId)
                when (networkResource.code) {
                    -1 -> {

                    }

                    200 -> {
                        _vacancyDetail.postValue(networkResource.data!!)
                    }

                    else -> {

                    }
                }
            }
        }
    }
}