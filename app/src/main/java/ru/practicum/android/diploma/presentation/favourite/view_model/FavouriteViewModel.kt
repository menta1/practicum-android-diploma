package ru.practicum.android.diploma.presentation.favourite.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.favourite.FavouriteInteractor
import ru.practicum.android.diploma.presentation.favourite.models.FavouriteState
import javax.inject.Inject

class FavouriteViewModel @Inject constructor(
    private val interactor: FavouriteInteractor
) : ViewModel() {
    private val favouriteStateLiveData = MutableLiveData<FavouriteState>()
    fun getFavouriteStateLiveData(): LiveData<FavouriteState> = favouriteStateLiveData
    fun initData() {
        favouriteStateLiveData.postValue(FavouriteState.Loading)
        viewModelScope.launch {
            try {
                val data = interactor.getAllVacancies()
                if (data.isEmpty()) {
                    favouriteStateLiveData.postValue(
                        FavouriteState.Empty
                    )
                } else {
                    favouriteStateLiveData.postValue(
                        FavouriteState.Content(data)
                    )
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                favouriteStateLiveData.postValue(
                    FavouriteState.Error
                )
            }
        }
    }
}