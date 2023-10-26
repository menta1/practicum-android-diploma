package ru.practicum.android.diploma.presentation.search.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.data.Constants.NO_INTERNET
import ru.practicum.android.diploma.data.Constants.OK_RESPONSE
import ru.practicum.android.diploma.data.Constants.SERVER_ERROR
import ru.practicum.android.diploma.domain.filter.FilterInteractor
import ru.practicum.android.diploma.domain.models.Filter
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.search.SearchInteractor
import ru.practicum.android.diploma.presentation.search.SearchModelStates
import ru.practicum.android.diploma.util.debounce
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    val interactor: SearchInteractor, private val filterInteractor: FilterInteractor
) : ViewModel() {
    private var currentPage: Int = 0
    private var maxPages: Int = 0
    private lateinit var searchText: String
    private var isNewPageLoaded: Boolean = false
    private var isNewSearch: Boolean = YES_NEW_SEARCH
    private val tempList = ArrayList<Vacancy>()
    private var lastSearchList: List<Vacancy> = emptyList()

    private val _usersFoundLiveData = MutableLiveData<String>().apply {
        postValue("")
    }
    val usersFoundLiveData: LiveData<String> = _usersFoundLiveData

    private val _searchStateLiveData = MutableLiveData<SearchModelStates>().apply {
        postValue(SearchModelStates.NoSearch)
    }
    val searchStateLiveData = _searchStateLiveData

    private val searchDebounce = debounce<Boolean>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) {
        search()
    }

    private var filter: Filter? = null

    private val _savedInput = MutableLiveData<String>()
    val savedInput: LiveData<String> = _savedInput

    private fun newSearch(answer: Boolean) {
        if (answer) {
            _searchStateLiveData.value = SearchModelStates.Loading
        } else {
            _searchStateLiveData.value = SearchModelStates.NewSearch
        }
    }

    fun search() {
        if (searchText.isNotBlank()) {
            Log.d("tag", "search()")
            newSearch(isNewSearch)
            viewModelScope.launch {
                interactor.search(searchText, currentPage, filter).collect { result ->
                    when (result.first.code) {
                        NO_INTERNET -> _searchStateLiveData.value = SearchModelStates.NoInternet

                        OK_RESPONSE -> {
                            if (result.second.page?.let { it < 1 } == true) {
                                _usersFoundLiveData.value = result.second.found.toString()
                            }
                            if (result.second.page?.let { it >= 1 } == true) {
                                Log.d("tag", " it >= 1 } == true")
                                tempList.addAll(result.first.data ?: emptyList())
                                lastSearchList = result.first.data ?: emptyList()
                                _searchStateLiveData.value = SearchModelStates.Content(tempList)
                            } else {
                                Log.d("tag", "else if")
                                lastSearchList = result.first.data ?: emptyList()
                                _searchStateLiveData.value =
                                    SearchModelStates.Content(result.first.data!!)
                            }

                            if (result.first.data.isNullOrEmpty()) {
                                _searchStateLiveData.value = SearchModelStates.FailedToGetList
                            }
                        }

                        SERVER_ERROR -> {
                            _searchStateLiveData.value = SearchModelStates.ServerError
                        }
                    }

                    currentPage = result.second.page?.plus(1) ?: 0
                    maxPages = result.second.pages ?: 0
                    if (result.second.found?.let { it <= 0 } == true) {
                        _searchStateLiveData.value = SearchModelStates.FailedToGetList
                    }
                    isNewPageLoaded = true
                }
            }
        }
    }

    fun onTextChangedInput(inputChar: CharSequence?) {
        if (::searchText.isInitialized) {
            Log.d("tag", "isInitialized")
            if (inputChar.toString().isBlank()) {
                Log.d("tag", "isInitialized isBlank()")
                searchText = inputChar.toString()
                _searchStateLiveData.value = SearchModelStates.NoSearch
            } else if (searchText != inputChar.toString()) {
                Log.d("tag", "isInitialized != inputChar.toString()")
                searchText = inputChar.toString()
                currentPage = 0
                isNewSearch = YES_NEW_SEARCH
                tempList.clear()
                searchDebounce(true)
            }
        } else {
            Log.d("tag", "else")
            isNewSearch = YES_NEW_SEARCH
            searchText = inputChar.toString()
            currentPage = 0
            tempList.clear()
            searchDebounce(true)
        }
    }

    fun searchWithNewText() {
        tempList.clear()
        search()
    }

    fun onLastItemReached() {
        if (maxPages != currentPage) {
            isNewSearch = NO_NEW_SEARCH
            if (isNewPageLoaded) {
                isNewPageLoaded = false
                search()
            }
        }
    }

    fun getFilter() {
        filter = filterInteractor.getFilter()
    }

    fun isFilterEmpty(): Boolean = filterInteractor.isFilterEmpty()

    fun startSearchIfNewFiltersSelected() {
        val isSearchingNow = filterInteractor.getSearchingMode()

        if (isSearchingNow && ::searchText.isInitialized && searchText.isNotBlank()) {
            currentPage = 0
            search()
        }
    }

    companion object {
        const val SEARCH_DEBOUNCE_DELAY = 2000L
        const val YES_NEW_SEARCH = true
        const val NO_NEW_SEARCH = false
    }
}