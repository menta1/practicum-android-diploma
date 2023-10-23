package ru.practicum.android.diploma.presentation.search.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private var isStartingTime = false

    private val _usersLiveData = MutableLiveData<List<Vacancy>>().apply {
        postValue(emptyList())
    }
    val usersLiveData: LiveData<List<Vacancy>> = _usersLiveData

    private val _usersFoundLiveData = MutableLiveData<String>().apply {
        postValue("")
    }
    val usersFoundLiveData: LiveData<String> = _usersFoundLiveData

    private val _viewStateLiveData = MutableLiveData<SearchModelState>().apply {
        postValue(SearchModelState.NoSearch)
    }
    val viewStateLiveData: LiveData<SearchModelState> = _viewStateLiveData

    private val searchDebounce = debounce<Boolean>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) {
        search()
    }

    private var filter: Filter? = null

    private val _savedInput = MutableLiveData<String>()
    val savedInput: LiveData<String> = _savedInput

    fun search() {
        if (searchText.isNotEmpty()) {
            viewModelScope.launch {
                if (currentPage == 0) _viewStateLiveData.value = SearchModelState.Loading
                interactor.search(searchText, currentPage, filter).collect { result ->
                    _viewStateLiveData.value = SearchModelState.Search
                    if (result.second.page?.let { it < 1 } == true) {
                        _usersFoundLiveData.value = result.second.found.toString()
                    }
                    when (result.first.code) {
                        -1 -> _viewStateLiveData.value = SearchModelState.NoInternet
                        200 -> {
                            _viewStateLiveData.value = SearchModelState.Loaded
                            if (result.second.page?.let { it >= 1 } == true) {
                                val tempFile = _usersLiveData.value?.plus(result.first.data!!)
                                _usersLiveData.value = tempFile
                            } else _usersLiveData.value = result.first.data!!
                            isNextPageLoading = true
                        }

                        else -> _viewStateLiveData.value = SearchModelState.FailedToGetList
                    }
                    currentPage = result.second.page?.plus(1) ?: 0
                    maxPages = result.second.pages ?: 0
                    if (result.second.found?.let { it <= 0 } == true) {
                        _viewStateLiveData.value = SearchModelState.FailedToGetList
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
                _viewStateLiveData.value = SearchModelState.NoSearch
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

    fun getStartingInfo(isNow: Boolean){
        isStartingTime = isNow
    }

    fun startSearchIfNewFiltersSelected(){
        if (isStartingTime){
            val savedInputFromData = filterInteractor.getSavedInput()
            if (savedInputFromData.isNotBlank()) _savedInput.value = savedInputFromData
        }
    }

    fun editSavedInput(input: String){

        if (input.isNotBlank()){
            filterInteractor.putSavedInput(input)
        }
        else filterInteractor.clearSavedInput()
    }

}