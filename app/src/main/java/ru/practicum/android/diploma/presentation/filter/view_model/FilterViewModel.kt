package ru.practicum.android.diploma.presentation.filter.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.filter.FilterInteractor
import ru.practicum.android.diploma.domain.models.Filter
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

    private var filter: Filter? = null

    private val _isAllowedToNavigate = MutableLiveData<Boolean>(false)
    val isAllowedToNavigate: LiveData<Boolean> = _isAllowedToNavigate

    private lateinit var defaultList: List<Region>


    fun getFilter() {

        val resultFromData = interactor.getFilter()

        _filterScreenState.postValue(if (resultFromData != null) {
            FilterScreenState.Content(
                countryName = resultFromData.countryName,
                regionName = resultFromData.regionName,
                industryName = resultFromData.industryName,
                expectedSalary = resultFromData.expectedSalary,
                isOnlyWithSalary = resultFromData.isOnlyWithSalary
            )

        } else FilterScreenState.Default)

        filter = resultFromData
    }

    fun getAllCountries() {
        _filterCountryScreenState.value = FilterCountryScreenState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            interactor.getAllCountries().collect { apiResult ->
                when (apiResult.code) {
                    -1 -> {
                        _filterCountryScreenState.postValue(FilterCountryScreenState.NoInternet)
                    }

                    200 -> {
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
                        -1 -> {
                            _filterRegionScreenState.postValue(FilterRegionScreenState.NoInternet)
                        }

                        200 -> {
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
                        -1 -> {
                            _filterRegionScreenState.postValue(FilterRegionScreenState.NoInternet)
                        }

                        200 -> {
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

    private fun addCountryWhenItIsNotSelected(regionId: String) {

        viewModelScope.launch(Dispatchers.IO) {
            interactor.getCountyByRegionId(regionId).collect { apiResult ->

                when (apiResult.code) {
                    -1 -> {
                        //подумать надо ли показывать ошибки и как
                    }

                    200 -> {
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
            interactor.getCountyByRegionId(filter?.regionId?:"").collect { apiResult ->

                when (apiResult.code) {
                    -1 -> {
                        //подумать надо ли показывать ошибки и как
                    }

                    200 -> {
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

    fun clearPlace(){
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

    companion object {
        const val BACK_TO_DEFAULT = 100L
    }
}