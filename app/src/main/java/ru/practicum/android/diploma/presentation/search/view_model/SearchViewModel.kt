package ru.practicum.android.diploma.presentation.search.view_model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.search.SearchInteractor
import ru.practicum.android.diploma.presentation.search.SearchModelState
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    val interactor: SearchInteractor
) : ViewModel() {
    private var currentPage: Int = 0
    private var maxPages: Int = 0
    private var debounce: Long = 2000L
    private lateinit var searchText: String
    private var isNextPageLoading = true

    private val _usersFlow: MutableStateFlow<List<Vacancy>> = MutableStateFlow(emptyList())
    val usersFlow: StateFlow<List<Vacancy>> = _usersFlow

    private val _usersFound: MutableStateFlow<String> = MutableStateFlow("")
    val usersFound: StateFlow<String> = _usersFound

    private val _viewState: MutableStateFlow<SearchModelState> =
        MutableStateFlow(SearchModelState.NoSearch)
    val viewState: StateFlow<SearchModelState> = _viewState

    //var usersFlow: Flow<List<Vacancy>>

    private val searchBy = MutableLiveData("")


    fun search(text: String, doneAction: Boolean) {
//        searchText = text
//        if (searchBy.value == text) return
        searchText = text
        searchBy.value = text
        debounce = if (doneAction) {
            0
        } else 2000L


        viewModelScope.launch {
            _viewState.value = SearchModelState.Loading
            interactor.search(text, currentPage).debounce(debounce).collect { result ->
                isNextPageLoading = false
                when (result.first.code) {
                    -1 -> _viewState.value = SearchModelState.NoInternet
                    200 -> {
                        _viewState.value = SearchModelState.Loaded
                        val tempFile = _usersFlow.value + result.first.data!!
                        _usersFlow.value = tempFile
                    }

                    else -> _viewState.value = SearchModelState.FailedToGetList
                }
                _usersFound.value = result.second.found.toString()
                currentPage = result.second.page?.plus(1) ?: 0
                maxPages = result.second.pages ?: 0
            }
        }
    }

    fun onLastItemReached() {
        Log.d("tag", "onLastItemReached")
        if (maxPages != currentPage && isNextPageLoading) {
            search(searchText, true)
        }
    }

    fun onClick(item: Vacancy) {

    }
}