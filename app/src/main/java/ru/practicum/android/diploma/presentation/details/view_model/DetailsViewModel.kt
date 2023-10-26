package ru.practicum.android.diploma.presentation.details.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import ru.practicum.android.diploma.data.Constants.NO_INTERNET
import ru.practicum.android.diploma.data.Constants.OK_RESPONSE
import ru.practicum.android.diploma.domain.details.DetailsInteractor
import ru.practicum.android.diploma.domain.models.VacancyDetail
import ru.practicum.android.diploma.domain.share.SharingInteractor
import ru.practicum.android.diploma.domain.share.model.EmailData
import ru.practicum.android.diploma.domain.share.model.PhoneData
import ru.practicum.android.diploma.domain.share.model.SharingData
import ru.practicum.android.diploma.presentation.details.models.DetailsState
import javax.inject.Inject

class DetailsViewModel @Inject constructor(
    private val detailsInteractor: DetailsInteractor,
    private val sharingInteractor: SharingInteractor,
) : ViewModel() {
    private val detailsStateLiveData = MutableLiveData<DetailsState>()
    fun getDetailsStateLiveData(): LiveData<DetailsState> = detailsStateLiveData

    private var currentVacancy: VacancyDetail? = null
    private var isFavourite: Boolean = false

    fun initData(vacancyId: String?) {
        detailsStateLiveData.postValue(DetailsState.Loading)
        if (vacancyId == null) {
            detailsStateLiveData.postValue(DetailsState.Error)
        } else {
            viewModelScope.launch {
                isFavourite = detailsInteractor.isVacancyInFavourites(vacancyId)

                if (isFavourite) {
                    currentVacancy = detailsInteractor.getFavouriteVacancy(vacancyId)
                    detailsStateLiveData.postValue(
                        DetailsState.Content(
                            currentVacancy!!,
                            isFavourite
                        )
                    )
                } else {

                    try {
                        val networkResource = detailsInteractor.getVacancyDetails(vacancyId)
                        when (networkResource.code) {
                            NO_INTERNET -> {
                                detailsStateLiveData.postValue(DetailsState.Error)
                            }

                            OK_RESPONSE -> {
                                currentVacancy = networkResource.data!!
                                detailsStateLiveData.postValue(
                                    DetailsState.Content(
                                        networkResource.data,
                                        isFavourite
                                    )
                                )
                            }

                            else -> {
                                detailsStateLiveData.postValue(DetailsState.Error)
                            }
                        }
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                        detailsStateLiveData.postValue(DetailsState.Error)
                    }

                }
            }
        }
    }

    fun setFavourite() {
        currentVacancy?.let {
            if (isFavourite) deleteVacancy(currentVacancy!!)
            else saveVacancy(currentVacancy!!)
        }
    }

    fun sharingVacancy() {
        currentVacancy?.let {
            sharingInteractor.sharingVacancy(
                SharingData(it.url)
            )
        }
    }

    fun employerPhone() {
        currentVacancy?.phone?.let { phone ->
            sharingInteractor.callPhone(
                PhoneData(phone = phone.first().number)
            )
        }
    }

    fun employerEmail() {
        currentVacancy?.email?.let { email ->
            sharingInteractor.sendEmail(
                EmailData(email = email)
            )
        }
    }

    private fun saveVacancy(vacancy: VacancyDetail) {
        viewModelScope.launch(Dispatchers.IO) {
            detailsInteractor.saveVacancy(vacancy)
            detailsStateLiveData.postValue(DetailsState.Content(currentVacancy!!, true))
            isFavourite = true
        }
    }

    private fun deleteVacancy(vacancy: VacancyDetail) {
        viewModelScope.launch(Dispatchers.IO) {
            detailsInteractor.deleteVacancy(vacancy)
            detailsStateLiveData.postValue(DetailsState.Content(currentVacancy!!, false))
            isFavourite = false
        }
    }
}