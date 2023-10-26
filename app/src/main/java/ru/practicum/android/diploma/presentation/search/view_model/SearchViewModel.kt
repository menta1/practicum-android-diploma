package ru.practicum.android.diploma.presentation.search.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.data.Constants.NO_INTERNET
import ru.practicum.android.diploma.data.Constants.OK_RESPONSE
import ru.practicum.android.diploma.domain.filter.FilterInteractor
import ru.practicum.android.diploma.domain.models.Filter
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.search.SearchInteractor
import ru.practicum.android.diploma.presentation.search.LoadingPageErrorStates
import ru.practicum.android.diploma.presentation.search.SearchModelState
import ru.practicum.android.diploma.util.debounce
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    val interactor: SearchInteractor,
    private val filterInteractor: FilterInteractor
) : ViewModel() {
    private var currentPage: Int = 0
    private var maxPages: Int = 0
    private lateinit var searchText: String
    private var isThereAnyProblem = false

    private val _loadingPageErrorState = MutableLiveData<LoadingPageErrorStates>()
    val loadingPageErrorState : LiveData<LoadingPageErrorStates> = _loadingPageErrorState

    private val _isNextPageLoading = MutableLiveData(false)
    val isNextPageLoading: LiveData<Boolean> = _isNextPageLoading

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
        if (searchText.isNotBlank()) {

            if (currentPage == 0) {
                _viewStateLiveData.value = SearchModelState.Loading
            }
            else {
                _isNextPageLoading.value = true
            }

            viewModelScope.launch {

                interactor.search(searchText, currentPage, filter).collect { result ->
                    _viewStateLiveData.value = SearchModelState.Search

                    when (result.first.code) {
                        NO_INTERNET ->{

                            if (isThereAnyProblem){
                                _viewStateLiveData.value = SearchModelState.Loaded
                                _isNextPageLoading.value = false
                                return@collect
                            }

                            if (_isNextPageLoading.value == true){
                                isThereAnyProblem = true
                                _viewStateLiveData.value = SearchModelState.Loaded
                                _isNextPageLoading.value = false
                                _loadingPageErrorState.value = LoadingPageErrorStates.NoInternet
                                delay(BACK_TO_DEFAULT_STATE_DELAY)
                                _loadingPageErrorState.value = LoadingPageErrorStates.Default
                                return@collect
                            }
                            else {
                                _viewStateLiveData.value = SearchModelState.NoInternet
                                return@collect
                            }
                        }
                        OK_RESPONSE -> {
                            if (result.second.page?.let { it < 1 } == true) {
                                _usersFoundLiveData.value = result.second.found.toString()
                            }
                            _viewStateLiveData.value = SearchModelState.Loaded
                            if (result.second.page?.let { it >= 1 } == true) {

                                val tempList = ArrayList<Vacancy>()
                                tempList.addAll(_usersLiveData.value?: emptyList())
                                tempList.addAll(result.first.data?: emptyList())
                                _usersLiveData.value = tempList

                                _isNextPageLoading.value = false
                                isThereAnyProblem = false
                            } else {
                                _usersLiveData.value = result.first.data!!
                                isThereAnyProblem = false
                            }
                        }

                        else -> {
                            if (isThereAnyProblem){
                                _viewStateLiveData.value = SearchModelState.Loaded
                                _isNextPageLoading.value = false
                                return@collect
                            }

                            if (_isNextPageLoading.value == true ){
                                isThereAnyProblem = true
                                _viewStateLiveData.value = SearchModelState.Loaded
                                _loadingPageErrorState.value = LoadingPageErrorStates.ServerError
                                _isNextPageLoading.value = false
                                delay(BACK_TO_DEFAULT_STATE_DELAY)
                                _loadingPageErrorState.value = LoadingPageErrorStates.Default
                                return@collect
                            }
                            else  {
                                _viewStateLiveData.value = SearchModelState.FailedToGetList
                                return@collect
                            }
                        }
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
        if (maxPages != currentPage && _isNextPageLoading.value == false) {
            search()
        }
    }

    fun getFilter() {
        filter = filterInteractor.getFilter()
    }

    fun isFilterEmpty(): Boolean = filterInteractor.isFilterEmpty()

    fun startSearchIfNewFiltersSelected(){
        val isSearchingNow = filterInteractor.getSearchingMode()

        if (isSearchingNow && ::searchText.isInitialized && searchText.isNotBlank()){
            currentPage = 0
            search()
        }
    }

    companion object{
        const val SEARCH_DEBOUNCE_DELAY = 2000L
        const val BACK_TO_DEFAULT_STATE_DELAY = 300L
    }
}