package ru.practicum.android.diploma.domain.filter

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Filter
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.models.Region
import ru.practicum.android.diploma.util.NetworkResource
import javax.inject.Inject

class FilterInteractorImpl @Inject constructor(
    private val repository: FilterRepository
) : FilterInteractor {

    override fun getAllCountries(): Flow<NetworkResource<List<Region>>> = repository.getAllCountries()

    override fun getAllRegionsInCountry(countryId: String): Flow<NetworkResource<List<Region>>> = repository.getAllRegionsInCountry(countryId)

    override fun getAllIndustries(): Flow<NetworkResource<List<Industry>>> = repository.getAllIndustries()

    override fun getAllPossibleRegions(): Flow<NetworkResource<List<Region>>>  = repository.getAllPossibleRegions()

    override fun getCountyByRegionId(regionId: String): Flow<NetworkResource<Region>>  = repository.getCountyByRegionId(regionId)

    override fun getFilter(): Filter? {
        return repository.getFilter()
    }

    override fun editCountryNameAndId(country: Region) {
        repository.editCountryNameAndId(country)
    }

    override fun editRegionNameAndId(region: Region) {
        repository.editRegionNameAndId(region)
    }

    override fun editIndustryNameAndId(industry: Industry) {
        repository.editIndustryNameAndId(industry)
    }

    override fun editExpectedSalary(expectedSalary: Int) {
        repository.editExpectedSalary(expectedSalary)
    }

    override fun editIsOnlyWithSalary(isOnlyWithSalary: Boolean) {
        repository.editIsOnlyWithSalary(isOnlyWithSalary)
    }

    override fun clearCountryNameAndId() {
        repository.clearCountryNameAndId()
    }

    override fun clearRegionNameAndId() {
        repository.clearRegionNameAndId()
    }

    override fun clearIndustryNameAndId() {
        repository.clearIndustryNameAndId()
    }

    override fun clearExpectedSalary() {
        repository.clearExpectedSalary()
    }

    override fun clearFilter() {
        repository.clearFilter()
    }

    override fun isFilterEmpty(): Boolean {
        return repository.isFilterEmpty()
    }
}