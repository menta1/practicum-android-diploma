package ru.practicum.android.diploma.presentation.search.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.filter.FilterInteractor
import ru.practicum.android.diploma.domain.models.Filter
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.search.SearchInteractor
import ru.practicum.android.diploma.presentation.search.SearchModelState
import ru.practicum.android.diploma.util.Constants.SEARCH_DEBOUNCE_DELAY
import ru.practicum.android.diploma.util.debounce
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    val interactor: SearchInteractor,
    private val filterInteractor: FilterInteractor
) : ViewModel() {
    private var currentPage: Int = 0
    private var maxPages: Int = 0
    private lateinit var searchText: String
    private var isNextPageLoading = true

    private val _usersFlow: MutableStateFlow<List<Vacancy>> = MutableStateFlow(emptyList())
    val usersFlow: StateFlow<List<Vacancy>> = _usersFlow

    private val _usersFound: MutableStateFlow<String> = MutableStateFlow("")
    val usersFound: StateFlow<String> = _usersFound

    private val _viewState: MutableStateFlow<SearchModelState> =
        MutableStateFlow(SearchModelState.NoSearch)
    val viewState: StateFlow<SearchModelState> = _viewState

    private val searchDebounce = debounce<Boolean>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) {
        search()
    }

    private var filter: Filter? = null

    fun search() {
        if (searchText.isNotEmpty()) {
            viewModelScope.launch {
                if (currentPage == 0) _viewState.value = SearchModelState.Loading
                interactor.search(searchText, currentPage, filter).collect { result ->
                    _viewState.value = SearchModelState.Search
                    if (result.second.page?.let { it < 1 } == true) {
                        _usersFound.value = result.second.found.toString()
                    }
                    when (result.first.code) {
                        -1 -> _viewState.value = SearchModelState.NoInternet
                        200 -> {
                            _viewState.value = SearchModelState.Loaded
                            if (result.second.page?.let { it >= 1 } == true) {
                                val tempFile = _usersFlow.value + result.first.data!!
                                _usersFlow.value = tempFile
                            } else _usersFlow.value = result.first.data!!
                            isNextPageLoading = true
                        }

                        else -> _viewState.value = SearchModelState.FailedToGetList
                    }
                    currentPage = result.second.page?.plus(1) ?: 0
                    maxPages = result.second.pages ?: 0
                    if (result.second.found?.let { it <= 0 } == true) {
                        Log.d("tag", "FailedToGetList")
                        _viewState.value = SearchModelState.FailedToGetList
                    }
                }
            }
        }
    }

    fun onTextChangedInput(inputChar: CharSequence?) {
        if (::searchText.isInitialized) {
            if (searchText != inputChar.toString()) {
                searchText = inputChar.toString()
                currentPage = 0
                searchDebounce(true)
            }
            if (searchText.isBlank()) {
                _viewState.value = SearchModelState.NoSearch
            }
        } else {
            searchText = inputChar.toString()
            currentPage = 0
            searchDebounce(true)
        }
    }

    fun onLastItemReached() {
        if (maxPages != currentPage && isNextPageLoading) {
            search()
            isNextPageLoading = false
        }
    }

    fun getFilter() {
        filter = filterInteractor.getFilter()
    }

    fun isFilterEmpty(): Boolean = filterInteractor.isFilterEmpty()

    fun onClick(item: Vacancy) {

    }
}