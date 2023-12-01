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

    private val _viewStateLiveData = MutableLiveData<SearchModelState>().apply {
        postValue(SearchModelState.NoSearch(isFilterEmpty()))
    }
    val viewStateLiveData: LiveData<SearchModelState> = _viewStateLiveData

    private val searchDebounce = debounce<Boolean>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) {
        search()
    }

    private var filter: Filter? = null
    private var isNextPageLoading = false
    private var currentVacanciesList: List<Vacancy> = emptyList()
    private var vacanciesNumber = ""

    fun search() {
        if (searchText.isNotBlank()) {

            if (currentPage == 0) {
                _viewStateLiveData.value = SearchModelState.Loading
            }
            else {
                _viewStateLiveData.value = SearchModelState.NextPageLoading(true)
                isNextPageLoading = true
            }

            viewModelScope.launch {

                interactor.search(searchText, currentPage, filter).collect { result ->
                    _viewStateLiveData.value = SearchModelState.Search

                    when (result.first.code) {
                        NO_INTERNET ->{

                            if (isThereAnyProblem){
                                _viewStateLiveData.value = SearchModelState.Loaded(isFilterEmpty())
                                _viewStateLiveData.value = SearchModelState.NextPageLoading(false)
                                isNextPageLoading = false
                                return@collect
                            }

                            if (isNextPageLoading){
                                isThereAnyProblem = true
                                _viewStateLiveData.value = SearchModelState.Loaded(isFilterEmpty())
                                _viewStateLiveData.value = SearchModelState.NextPageLoading(false)
                                isNextPageLoading = false
                                _viewStateLiveData.value = SearchModelState.NoInternetWhilePaging
                                delay(BACK_TO_DEFAULT_STATE_DELAY)
                                _viewStateLiveData.value = SearchModelState.Loaded(isFilterEmpty())
                                return@collect
                            }
                            else {
                                vacanciesNumber = ""
                                _viewStateLiveData.value = SearchModelState.NoInternet
                                return@collect
                            }
                        }
                        OK_RESPONSE -> {

                            _viewStateLiveData.value = SearchModelState.Loaded(isFilterEmpty())
                            if (result.second.page?.let { it >= 1 } == true) {
                                _viewStateLiveData.value = SearchModelState.NextPageLoading(false)
                                val tempList = ArrayList<Vacancy>()
                                tempList.addAll(currentVacanciesList)
                                tempList.addAll(result.first.data?: emptyList())

                                _viewStateLiveData.value = SearchModelState.Content(
                                    vacancies = tempList,
                                    vacanciesNumber = result.second.found.toString(),
                                    isFirstLaunch = false
                                )
                                vacanciesNumber = result.second.found.toString()
                                currentVacanciesList = tempList


                                isNextPageLoading = false
                                isThereAnyProblem = false
                            } else {
                                _viewStateLiveData.value = SearchModelState.Loaded(isFilterEmpty())
                                currentVacanciesList = result.first.data?: emptyList()
                                vacanciesNumber = result.second.found.toString()

                                _viewStateLiveData.value = SearchModelState.Content(
                                    vacancies =  result.first.data?: emptyList(),
                                    vacanciesNumber = result.second.found.toString(),
                                    isFirstLaunch = false
                                )

                                isThereAnyProblem = false
                            }
                        }

                        else -> {
                            if (isThereAnyProblem){

                                _viewStateLiveData.value = SearchModelState.Loaded(isFilterEmpty())
                                _viewStateLiveData.value = SearchModelState.NextPageLoading(false)
                                isNextPageLoading = false
                                return@collect
                            }

                            if (isNextPageLoading){
                                isThereAnyProblem = true
                                _viewStateLiveData.value = SearchModelState.Loaded(isFilterEmpty())
                                _viewStateLiveData.value = SearchModelState.ServerErrorWhilePaging
                                _viewStateLiveData.value = SearchModelState.NextPageLoading(false)
                                isNextPageLoading = false
                                delay(BACK_TO_DEFAULT_STATE_DELAY)
                                _viewStateLiveData.value = SearchModelState.Loaded(isFilterEmpty())
                                return@collect
                            }
                            else  {
                                vacanciesNumber = ""
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
                isThereAnyProblem = false
                searchDebounce(true)
            }
            if (searchText.isBlank()) {
                _viewStateLiveData.value = SearchModelState.NoSearch(isFilterEmpty())
            }
        } else {
            searchText = inputChar.toString()
            currentPage = 0
            isThereAnyProblem = false
            searchDebounce(true)
        }
    }

    fun onLastItemReached() {
        if (maxPages != currentPage && !isNextPageLoading/*_isNextPageLoading.value == false*/) {
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

    fun refreshFirstLaunch() {
        if (::searchText.isInitialized && searchText.isNotBlank() && vacanciesNumber.isNotBlank())//+ в каких случаях еще не отрправлять
            _viewStateLiveData.value = SearchModelState.Content(
                vacancies = currentVacanciesList,
                vacanciesNumber = vacanciesNumber,
                isFirstLaunch = true
            )
    }


    companion object{
        const val SEARCH_DEBOUNCE_DELAY = 2000L
        const val BACK_TO_DEFAULT_STATE_DELAY = 300L
    }
}