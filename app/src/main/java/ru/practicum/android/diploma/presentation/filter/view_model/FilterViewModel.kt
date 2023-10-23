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
import ru.practicum.android.diploma.presentation.filter.models.FilterRegionScreenState
import ru.practicum.android.diploma.presentation.filter.models.FilterScreenState
import javax.inject.Inject

class FilterViewModel @Inject constructor(private val interactor: FilterInteractor) : ViewModel() {

    private val _filterScreenState = MutableLiveData<FilterScreenState>()
    val filterScreenState: LiveData<FilterScreenState> = _filterScreenState

    private val _filterCountryScreenState = MutableLiveData<FilterCountryScreenState>()
    val filterCountryScreenState: LiveData<FilterCountryScreenState> = _filterCountryScreenState

    private val _filterRegionScreenState = MutableLiveData<FilterRegionScreenState>()
    val filterRegionScreenState: LiveData<FilterRegionScreenState> = _filterRegionScreenState

    private val _countries = MutableLiveData<List<Region>>()
    val countries: LiveData<List<Region>> = _countries

    private val _isAllowedToNavigate = MutableLiveData<Boolean>(false)
    val isAllowedToNavigate: LiveData<Boolean> = _isAllowedToNavigate

    private val _isSelectionButtonVisible = MutableLiveData<Boolean>(false)
    val isSelectionButtonVisible: LiveData<Boolean> = _isSelectionButtonVisible

    private val _isTimeToHideKeyboard = MutableLiveData<Boolean>(false)
    val isTimeToHideKeyboard: LiveData<Boolean> = _isTimeToHideKeyboard

    private val _industries = MutableLiveData<List<Industry>>()
    val industries: LiveData<List<Industry>> = _industries

    private  var defaultList: List<Region> = emptyList()
    private  var industriesDefaultList: List<Industry> = emptyList()

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
                        _filterCountryScreenState.postValue(FilterCountryScreenState.Content)
                        _countries.postValue(apiResult.data ?: emptyList())
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
                                    isListEmpty = false
                                )
                            )
                            _countries.postValue(apiResult.data ?: emptyList())
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
                                    isListEmpty = false
                                )
                            )
                            _countries.postValue(apiResult.data ?: emptyList())
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
        _filterRegionScreenState.value = FilterRegionScreenState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            interactor.getAllIndustries().collect { apiResult ->
                when (apiResult.code) {
                    NO_INTERNET -> {
                        _filterRegionScreenState.postValue(FilterRegionScreenState.NoInternet)
                        _isSelectionButtonVisible.postValue(false)
                    }

                    OK_RESPONSE -> {
                        _filterRegionScreenState.postValue(
                            FilterRegionScreenState.Content(
                                isListEmpty = false
                            )
                        )
                        _industries.postValue(apiResult.data ?: emptyList())
                        industriesDefaultList = apiResult.data ?: emptyList()
                    }

                    else -> {
                        _filterRegionScreenState.postValue(FilterRegionScreenState.UnableToGetResult)
                        _isSelectionButtonVisible.postValue(false)
                    }
                }
            }
        }

    }

    private fun addCountryWhenItIsNotSelected(regionId: String) {

        viewModelScope.launch(Dispatchers.IO) {
            interactor.getCountyByRegionId(regionId).collect { apiResult ->

                when (apiResult.code) {
                    NO_INTERNET -> {
                        //подумать надо ли показывать ошибки и как
                    }

                    OK_RESPONSE -> {
                        editCountry(apiResult.data!!)
                        _isAllowedToNavigate.postValue(true)
                        delay(BACK_TO_DEFAULT)
                        _isAllowedToNavigate.postValue(false)
                    }

                    else -> {
                        //подумать надо ли показывать ошибки и как
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
                        //подумать надо ли показывать ошибки и как
                    }

                    OK_RESPONSE -> {
                        editCountry(apiResult.data!!)
                        _isAllowedToNavigate.postValue(true)
                        delay(BACK_TO_DEFAULT)
                        _isAllowedToNavigate.postValue(false)
                    }

                    else -> {
                        //подумать надо ли показывать ошибки и как
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
        getFilter()
    }

    fun clearFiler() {
        interactor.clearFilter()
        getFilter()
    }

    fun editExpectedSalary(input: Int) {
        interactor.editExpectedSalary(input)
        getFilter()
    }

    fun editIsOnlyWithSalary(isOnlyWithSalary: Boolean) {
        interactor.editIsOnlyWithSalary(isOnlyWithSalary)
        getFilter()
    }

    fun editCountry(country: Region) {
        interactor.editCountryNameAndId(country)
    }

    fun editRegion(region: Region) {
        if (filter?.countryId != null) {
            interactor.editRegionNameAndId(region)
            _isAllowedToNavigate.value = true
        } else {
            addCountryWhenItIsNotSelected(region.id)
            interactor.editRegionNameAndId(region)
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
        if (_filterRegionScreenState.value == FilterRegionScreenState.NoInternet || _filterRegionScreenState.value == FilterRegionScreenState.UnableToGetResult) {
            hideKeyboard()
            return
        }
        viewModelScope.launch(Dispatchers.IO) {

            var filteredList = mutableListOf<Region>()
            filteredList = defaultList.filter {
                it.name.contains(searchInput, true)
            }.sortedBy { it.name }.toMutableList()

            if (filteredList.isEmpty()) {
                _filterRegionScreenState.postValue(FilterRegionScreenState.Content(true))
                _countries.postValue(filteredList)
            } else {
                _filterRegionScreenState.postValue(FilterRegionScreenState.Content(false))
                _countries.postValue(filteredList)
            }
        }
    }

    fun checkIfSelectionDoneProperly(): Boolean {
        return if (filter?.countryId == null && filter?.regionId == null) true
        else if (filter?.countryId != null && filter?.regionId != null) true
        else filter?.countryId != null
    }

    fun handleRadioButtonsChecks(adapterPosition: Int) {
        _isSelectionButtonVisible.value = true
        if (previousCheckedPosition >= 0) {
            _industries.value?.get(previousCheckedPosition)?.isChecked = false
        }
        _industries.value?.get(adapterPosition)?.isChecked = true
        previousCheckedPosition = adapterPosition
        hideKeyboard()
    }

    fun filterIndustryList(searchInput: String) {
        if (_filterRegionScreenState.value == FilterRegionScreenState.NoInternet || _filterRegionScreenState.value == FilterRegionScreenState.UnableToGetResult){
            hideKeyboard()
            return
        }
        viewModelScope.launch(Dispatchers.IO) {

            var filteredList = mutableListOf<Industry>()
            filteredList = industriesDefaultList.filter {
                it.name.contains(searchInput, true)
            }.sortedBy { it.name }.toMutableList()

            if (filteredList.isEmpty()) {
                _filterRegionScreenState.postValue(FilterRegionScreenState.Content(true))
                _industries.postValue(filteredList)
                _isSelectionButtonVisible.postValue(false)
            } else {
                if (previousCheckedPosition >= 0) {
                    _isSelectionButtonVisible.postValue(true)
                } else {
                    _isSelectionButtonVisible.postValue(false)
                }
                _filterRegionScreenState.postValue(FilterRegionScreenState.Content(false))
                _industries.postValue(filteredList)
            }
        }
    }

    private fun hideKeyboard(){
        viewModelScope.launch {
            _isTimeToHideKeyboard.postValue(true)
            delay(BACK_TO_DEFAULT)
            _isTimeToHideKeyboard.postValue(false)
        }
    }

    companion object {
        const val BACK_TO_DEFAULT = 100L
    }
}