package ru.practicum.android.diploma.presentation.similar.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.similar.SimilarInteractor
import ru.practicum.android.diploma.presentation.similar.models.SimilarState
import javax.inject.Inject

class SimilarViewModel @Inject constructor(
    private val interactor: SimilarInteractor
): ViewModel() {
    private val similarStateLiveData = MutableLiveData<SimilarState>()
    fun getSimilarStateLiveData(): LiveData<SimilarState> = similarStateLiveData
    private var currentPage = 0

    fun init(idVacancy: String?) {
        similarStateLiveData.postValue(SimilarState.Loading)
        idVacancy?.let {
            viewModelScope.launch {
                interactor.getSimilarVacancy(idVacancy, currentPage).collect { result ->
                    when(result.first.code) {
                        -1 -> {
                            similarStateLiveData.postValue(SimilarState.NoInternet)
                        }
                        200 -> {
                            result.first.data?.let { data ->
                                similarStateLiveData.postValue(
                                    SimilarState.Content(data)
                                )
                            }
                        }
                        else -> {
                            similarStateLiveData.postValue(SimilarState.Error)
                        }
                    }
                }
            }
        } ?: similarStateLiveData.postValue(SimilarState.Error)
    }
}