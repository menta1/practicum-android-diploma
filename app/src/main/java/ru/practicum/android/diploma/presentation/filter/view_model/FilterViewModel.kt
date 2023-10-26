package ru.practicum.android.diploma.presentation.filter.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.data.Constants.NO_INTERNET
import ru.practicum.android.diploma.data.Constants.OK_RESPONSE
import ru.practicum.android.diploma.domain.filter.FilterInteractor
import ru.practicum.android.diploma.domain.models.Filter
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.models.Region
import ru.practicum.android.diploma.presentation.filter.models.FilterCountryScreenState
import ru.practicum.android.diploma.presentation.filter.models.FilterIndustryScreenState
import ru.practicum.android.diploma.presentation.filter.models.FilterRegionScreenState
import ru.practicum.android.diploma.presentation.filter.models.FilterScreenState
import javax.inject.Inject

class FilterViewModel @Inject constructor(private val interactor: FilterInteractor) : ViewModel() {

    private val _filterIndustryScreenState = MutableLiveData<FilterIndustryScreenState>()
    val filterIndustryScreenState: LiveData<FilterIndustryScreenState> = _filterIndustryScreenState

    private val _filterScreenState = MutableLiveData<FilterScreenState>()
    val filterScreenState: LiveData<FilterScreenState> = _filterScreenState

    private val _filterCountryScreenState = MutableLiveData<FilterCountryScreenState>()
    val filterCountryScreenState: LiveData<FilterCountryScreenState> = _filterCountryScreenState

    private val _filterRegionScreenState = MutableLiveData<FilterRegionScreenState>()
    val filterRegionScreenState: LiveData<FilterRegionScreenState> = _filterRegionScreenState

    private val _isAllowedToNavigate = MutableLiveData<Boolean>(false)
    val isAllowedToNavigate: LiveData<Boolean> = _isAllowedToNavigate

    private val _isSelectionButtonVisible = MutableLiveData<Boolean>(false)
    val isSelectionButtonVisible: LiveData<Boolean> = _isSelectionButtonVisible

    private var defaultList: List<Region> = emptyList()
    private var industriesDefaultList: List<Industry> = emptyList()
    private var industriesCurrentList: List<Industry> = emptyList()

    private var filter: Filter? = null

    private var previousCheckedPosition = -1


    fun getFilter() {

        val resultFromData = interactor.getFilter()

        _filterScreenState.postValue(
            if (resultFromData != null) {
                FilterScreenState.Content(
                    countryName = resultFromData.countryName,
                    regionName = resultFromData.regionName,
                    industryName = resultFromData.industryName,
                    expectedSalary = resultFromData.expectedSalary,
                    isOnlyWithSalary = resultFromData.isOnlyWithSalary
                )

            } else FilterScreenState.Default
        )

        filter = resultFromData
    }

    fun getAllCountries() {
        _filterCountryScreenState.value = FilterCountryScreenState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            interactor.getAllCountries().collect { apiResult ->
                when (apiResult.code) {
                    NO_INTERNET -> {
                        _filterCountryScreenState.postValue(FilterCountryScreenState.NoInternet)
                    }

                    OK_RESPONSE -> {
                        _filterCountryScreenState.postValue(FilterCountryScreenState.Content(
                            apiResult.data ?: emptyList()
                        ))

                    }

                    else -> {
                        _filterCountryScreenState.postValue(FilterCountryScreenState.UnableToGetResult)
                    }
                }
            }
        }
    }

    fun getAllRegions() {
        _filterRegionScreenState.value = FilterRegionScreenState.Loading

        viewModelScope.launch(Dispatchers.IO) {

            val countryId = interactor.getFilter()?.countryId

            if (countryId != null) {
                interactor.getAllRegionsInCountry(countryId).collect { apiResult ->
                    when (apiResult.code) {
                        NO_INTERNET -> {
                            _filterRegionScreenState.postValue(FilterRegionScreenState.NoInternet)
                        }

                        OK_RESPONSE -> {
                            _filterRegionScreenState.postValue(
                                FilterRegionScreenState.Content(
                                    apiResult.data ?: emptyList()
                                )
                            )
                            defaultList = apiResult.data ?: emptyList()
                        }

                        else -> {
                            _filterRegionScreenState.postValue(FilterRegionScreenState.UnableToGetResult)
                        }
                    }
                }
            } else {
                interactor.getAllPossibleRegions().collect { apiResult ->
                    when (apiResult.code) {
                        NO_INTERNET -> {
                            _filterRegionScreenState.postValue(FilterRegionScreenState.NoInternet)
                        }

                        OK_RESPONSE -> {
                            _filterRegionScreenState.postValue(
                                FilterRegionScreenState.Content(
                                    apiResult.data ?: emptyList()
                                )
                            )
                            defaultList = apiResult.data ?: emptyList()
                        }

                        else -> {
                            _filterRegionScreenState.postValue(FilterRegionScreenState.UnableToGetResult)
                        }
                    }
                }
            }
        }

    }

    fun getAllIndustries() {
        _filterIndustryScreenState.value = FilterIndustryScreenState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            interactor.getAllIndustries().collect { apiResult ->
                when (apiResult.code) {
                    NO_INTERNET -> {
                        _filterIndustryScreenState.postValue(FilterIndustryScreenState.NoInternet)
                    }
                    OK_RESPONSE -> {
                        _filterIndustryScreenState.postValue(
                            FilterIndustryScreenState.Content(industries = apiResult.data?: emptyList(), isSelected = false)
                        )
                        industriesDefaultList = apiResult.data ?: emptyList()
                        industriesCurrentList = apiResult.data ?: emptyList()

                    }

                    else -> {
                        _filterIndustryScreenState.postValue(FilterIndustryScreenState.UnableToGetResult)

                    }
                }
            }
        }

    }

    private fun addCountryWhenItIsNotSelected(region: Region) {

        viewModelScope.launch(Dispatchers.IO) {
            interactor.getCountyByRegionId(region.id).collect { apiResult ->

                when (apiResult.code) {
                    NO_INTERNET -> {
                        _filterRegionScreenState.postValue(FilterRegionScreenState.EscapeScreen)
                        _isAllowedToNavigate.postValue(true)
                        delay(BACK_TO_DEFAULT)
                        _isAllowedToNavigate.postValue(false)
                    }

                    OK_RESPONSE -> {

                        editCountryWithoutCheck(apiResult.data!!)
                        _filterRegionScreenState.postValue(FilterRegionScreenState.EscapeScreen)
                        _isAllowedToNavigate.postValue(true)
                        delay(BACK_TO_DEFAULT)
                        _isAllowedToNavigate.postValue(false)
                    }

                    else -> {
                        _filterRegionScreenState.postValue(FilterRegionScreenState.EscapeScreen)
                        _isAllowedToNavigate.postValue(true)
                        delay(BACK_TO_DEFAULT)
                        _isAllowedToNavigate.postValue(false)
                    }
                }
            }
        }
    }

    fun addCountryWhenItIsNotSelected() {

        viewModelScope.launch(Dispatchers.IO) {
            interactor.getCountyByRegionId(filter?.regionId ?: "").collect { apiResult ->

                when (apiResult.code) {
                    NO_INTERNET -> {
                        _filterRegionScreenState.postValue(FilterRegionScreenState.EscapeScreen)
                        _isAllowedToNavigate.postValue(true)
                        delay(BACK_TO_DEFAULT)
                        _isAllowedToNavigate.postValue(false)
                    }

                    OK_RESPONSE -> {
                        editCountry(apiResult.data!!)
                        _filterRegionScreenState.postValue(FilterRegionScreenState.EscapeScreen)
                        _isAllowedToNavigate.postValue(true)
                        delay(BACK_TO_DEFAULT)
                        _isAllowedToNavigate.postValue(false)
                    }

                    else -> {
                        _filterRegionScreenState.postValue(FilterRegionScreenState.EscapeScreen)
                        _isAllowedToNavigate.postValue(true)
                        delay(BACK_TO_DEFAULT)
                        _isAllowedToNavigate.postValue(false)
                    }
                }
            }
        }
    }

    fun clearWorkPlace() {
        interactor.clearCountryNameAndId()
        getFilter()
    }

    fun clearIndustry() {
        interactor.clearIndustryNameAndId()
        getFilter()
    }

    fun clearExpectedSalary() {
        interactor.clearExpectedSalary()
    }

    fun clearFiler() {
        interactor.clearFilter()
        getFilter()
    }

    fun editExpectedSalary(input: CharSequence?) {
        interactor.editExpectedSalary(input)
        _isSelectionButtonVisible.postValue(!input.isNullOrBlank())
    }

    fun editIsOnlyWithSalary(isOnlyWithSalary: Boolean) {
        interactor.editIsOnlyWithSalary(isOnlyWithSalary)
        getFilter()
    }

    fun editCountry(country: Region) {
        interactor.editCountryNameAndId(country)
    }

    private fun editCountryWithoutCheck(country: Region){
        interactor.editCountryNameAndIdWithoutCheck(country)
    }

    fun editRegion(region: Region) {

        if (filter?.countryId != null) {
            interactor.editRegionNameAndId(region)
            _filterRegionScreenState.postValue(FilterRegionScreenState.EscapeScreen)
            _isAllowedToNavigate.value = true
        } else {
            interactor.editRegionNameAndId(region)
            addCountryWhenItIsNotSelected(region)
        }
    }

    fun editIndustry(industry: Industry) {
        interactor.editIndustryNameAndId(industry)
    }

    fun clearPlace() {
        interactor.clearPlace()
        getFilter()
    }

    fun clearCountry() {
        interactor.clearCountryNameAndId()
        getFilter()
    }

    fun clearRegion() {
        interactor.clearRegionNameAndId()
        getFilter()
    }

    fun filterList(searchInput: String) {
        if (_filterRegionScreenState.value == FilterRegionScreenState.NoInternet){
            _filterRegionScreenState.value = FilterRegionScreenState.NoInternet
            return
        }
        else if(_filterRegionScreenState.value == FilterRegionScreenState.UnableToGetResult){
            _filterRegionScreenState.value = FilterRegionScreenState.UnableToGetResult
            return
        }

        viewModelScope.launch(Dispatchers.IO) {

            var filteredList = mutableListOf<Region>()
            filteredList = defaultList.filter {
                it.name.contains(searchInput, true)
            }.sortedBy { it.name }.toMutableList()

            _filterRegionScreenState.postValue(FilterRegionScreenState.Content(filteredList))
        }
    }

    fun checkIfSelectionDoneProperly(): Boolean {
        return if (filter?.countryId == null && filter?.regionId == null) true
        else if (filter?.countryId != null && filter?.regionId != null) true
        else filter?.countryId != null
    }


    fun handleRadioButtonsChecks(adapterPosition: Int) {
        //_isSelectionButtonVisible.value = true
        if (previousCheckedPosition >= 0) {
            industriesCurrentList[previousCheckedPosition].isChecked = false
        }
        industriesCurrentList[adapterPosition].isChecked = true
        previousCheckedPosition = adapterPosition
        _filterIndustryScreenState.postValue(FilterIndustryScreenState.Content(industriesCurrentList,true))

    }

    fun filterIndustryList(searchInput: String) {
        if (_filterIndustryScreenState.value == FilterIndustryScreenState.NoInternet){
            _filterIndustryScreenState.value = FilterIndustryScreenState.NoInternet
            return
        }
        else if(_filterIndustryScreenState.value == FilterIndustryScreenState.UnableToGetResult){
            _filterIndustryScreenState.value = FilterIndustryScreenState.UnableToGetResult
            return
        }

        viewModelScope.launch(Dispatchers.IO) {

            var filteredList = mutableListOf<Industry>()
            filteredList = industriesDefaultList.filter {
                it.name.contains(searchInput, true)
            }.sortedBy { it.name }.toMutableList()

            if (filteredList.isEmpty()) {
                industriesCurrentList = filteredList
                _filterIndustryScreenState.postValue(FilterIndustryScreenState.Content(industriesCurrentList,false))

            } else {

                if (filteredList.filter { it.isChecked  }.isNotEmpty()){
                    previousCheckedPosition = filteredList.indexOfFirst { it.isChecked }
                    industriesCurrentList = filteredList
                    _filterIndustryScreenState.postValue(FilterIndustryScreenState.Content(industriesCurrentList,true))
                }
                else{
                    industriesCurrentList = filteredList
                    _filterIndustryScreenState.postValue(FilterIndustryScreenState.Content(industriesCurrentList,false))
                }

            }
        }
    }

    fun putSearchingMode(isSearchingNow:Boolean){
        interactor.putSearchMode(isSearchingNow)
    }

    companion object {
        const val BACK_TO_DEFAULT = 100L
    }
}