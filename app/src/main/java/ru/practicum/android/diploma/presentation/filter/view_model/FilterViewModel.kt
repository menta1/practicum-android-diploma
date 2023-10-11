package ru.practicum.android.diploma.presentation.filter.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.domain.filter.FilterInteractor
import ru.practicum.android.diploma.presentation.filter.models.FilterScreenState
import javax.inject.Inject

class FilterViewModel @Inject constructor(private val interactor: FilterInteractor) : ViewModel() {

    private val _filterScreenState = MutableLiveData<FilterScreenState>()
    val filterScreenState: LiveData<FilterScreenState> = _filterScreenState


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

    fun clearWorkPlace(){
        interactor.clearCountryNameAndId()
        getFilter()
    }

    fun clearIndustry(){
        interactor.clearIndustryNameAndId()
        getFilter()
    }

    fun clearExpectedSalary(){
        interactor.clearExpectedSalary()
        getFilter()
    }

    fun clearFiler(){
        interactor.clearFilter()
        getFilter()
    }

    fun editExpectedSalary(input: Int){
        interactor.editExpectedSalary(input)
        getFilter()
    }

    fun editIsOnlyWithSalary(isOnlyWithSalary: Boolean){
        interactor.editIsOnlyWithSalary(isOnlyWithSalary)
        getFilter()
    }

}