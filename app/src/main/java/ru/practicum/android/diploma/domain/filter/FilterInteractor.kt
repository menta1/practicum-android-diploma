package ru.practicum.android.diploma.domain.filter

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Filter
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.models.Region
import ru.practicum.android.diploma.util.NetworkResource

interface FilterInteractor {

    fun getAllCountries(): Flow<NetworkResource<List<Region>>>
    fun getAllRegionsInCountry(countryId: String): Flow<NetworkResource<List<Region>>>
    fun getAllIndustries():Flow<NetworkResource<List<Industry>>>

    fun getFilter(): Filter?

    fun editCountryNameAndId(country: Region)
    fun editRegionNameAndId(region: Region)
    fun editIndustryNameAndId(industry: Industry)
    fun editExpectedSalary(expectedSalary: Int)
    fun editIsOnlyWithSalary(isOnlyWithSalary: Boolean)

    fun clearCountryNameAndId()
    fun clearRegionNameAndId()
    fun clearIndustryNameAndId()
    fun clearExpectedSalary()
    fun clearFilter()

    fun isFilterEmpty(): Boolean
}