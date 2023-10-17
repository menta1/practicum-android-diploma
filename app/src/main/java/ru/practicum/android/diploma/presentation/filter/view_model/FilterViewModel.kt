package ru.practicum.android.diploma.presentation.filter.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.filter.FilterInteractor
import ru.practicum.android.diploma.domain.models.Region
import ru.practicum.android.diploma.presentation.filter.models.FilterCountryScreenState
import ru.practicum.android.diploma.presentation.filter.models.FilterScreenState
import javax.inject.Inject

class FilterViewModel @Inject constructor(private val interactor: FilterInteractor) : ViewModel() {

    private val _filterScreenState = MutableLiveData<FilterScreenState>()
    val filterScreenState: LiveData<FilterScreenState> = _filterScreenState

    private val _filterCountryScreenState = MutableLiveData<FilterCountryScreenState>()
    val filterCountryScreenState: LiveData<FilterCountryScreenState> = _filterCountryScreenState

    private val _countries = MutableLiveData<List<Region>>()
    val countries: LiveData<List<Region>> = _countries


    fun getFilter() {

        val resultFromData = interactor.getFilter()

        _filterScreenState.value = if (resultFromData != null) {
            FilterScreenState.Content(
                countryName = resultFromData.countryName,
                regionName = resultFromData.regionName,
                industryName = resultFromData.industryName,
                expectedSalary = resultFromData.expectedSalary,
                isOnlyWithSalary = resultFromData.isOnlyWithSalary
            )

        } else FilterScreenState.Default
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
                    _countries.postValue(apiResult.data?: emptyList())
                    }

                    else -> {
                        _filterCountryScreenState.postValue(FilterCountryScreenState.UnableToGetResult)
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

    fun editCountry(country: Region){
        interactor.editCountryNameAndId(country)
    }

    fun clearCountry() {
        interactor.clearCountryNameAndId()
        getFilter()
    }

    fun clearRegion() {
        interactor.clearRegionNameAndId()
        getFilter()
    }
}