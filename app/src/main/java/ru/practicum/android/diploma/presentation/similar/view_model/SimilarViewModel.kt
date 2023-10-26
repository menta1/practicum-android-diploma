package ru.practicum.android.diploma.presentation.similar.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.data.Constants.NO_INTERNET
import ru.practicum.android.diploma.data.Constants.OK_RESPONSE
import ru.practicum.android.diploma.domain.similar.SimilarInteractor
import ru.practicum.android.diploma.presentation.similar.models.SimilarState
import javax.inject.Inject

class SimilarViewModel @Inject constructor(
    private val interactor: SimilarInteractor
): ViewModel() {
    private val similarStateLiveData = MutableLiveData<SimilarState>()
    fun getSimilarStateLiveData(): LiveData<SimilarState> = similarStateLiveData
    private var currentPage = 0
    private var maxPages = 0
    private var currentVacancyId: String? = null

    fun init(idVacancy: String?) {
        similarStateLiveData.postValue(SimilarState.Loading)
        currentVacancyId = idVacancy
        currentVacancyId?.let {
            searchSimilarVacancy(it)
        } ?: similarStateLiveData.postValue(SimilarState.Error)
    }

    private fun searchSimilarVacancy(idVacancy: String) {
        viewModelScope.launch {
            interactor.getSimilarVacancy(idVacancy, currentPage).collect { result ->
                when(result.first.code) {
                    NO_INTERNET -> {
                        similarStateLiveData.postValue(SimilarState.NoInternet)
                    }
                    OK_RESPONSE -> {
                        result.first.data?.let { data ->
                            similarStateLiveData.postValue(
                                SimilarState.Content(data)
                            )
                        }
                        currentPage = result.second.page?.plus(1) ?: 0
                        maxPages = result.second.pages ?: 0
                    }
                    else -> {
                        similarStateLiveData.postValue(SimilarState.Error)
                    }
                }
            }
        }
    }

    fun onLastItemReached() {
        if (maxPages != currentPage) {
            searchSimilarVacancy(currentVacancyId!!)
        } else {
            similarStateLiveData.postValue(
                SimilarState.Empty
            )
        }
    }
}