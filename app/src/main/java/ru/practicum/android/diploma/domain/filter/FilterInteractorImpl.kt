package ru.practicum.android.diploma.domain.filter

import android.util.Log
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.models.Region
import ru.practicum.android.diploma.util.NetworkResource
import ru.practicum.android.diploma.util.Resource
import javax.inject.Inject

class FilterInteractorImpl @Inject constructor(
    private val repository: FilterRepository
) : FilterInteractor {

    override fun getAllCountries(): Flow<NetworkResource<List<Region>>> = repository.getAllCountries()

    override fun getAllRegionsInCountry(countryId: String): Flow<NetworkResource<List<Region>>> = repository.getAllRegionsInCountry(countryId)

    override fun getAllIndustries(): Flow<NetworkResource<List<Industry>>> = repository.getAllIndustries()
}