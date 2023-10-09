package ru.practicum.android.diploma.presentation.search.view_model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.search.SearchInteractor
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    interactor: SearchInteractor
): ViewModel() {

    val usersFlow: Flow<PagingData<Vacancy>>

    private val searchBy = MutableLiveData("")

    init {
        usersFlow = searchBy.asFlow()
            .debounce(500)
            .flatMapLatest {
                interactor.search(it)
            }
            .cachedIn(viewModelScope)
    }

    fun search(text: String){
        Log.d("tag", text)
        if (searchBy.value == text) return
        searchBy.value = text
        Log.d("tag","searchBy " + searchBy.value.toString())
    }
}