package ru.practicum.android.diploma.domain.filter

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.models.Region
import ru.practicum.android.diploma.util.NetworkResource

interface FilterRepository {

    fun getAllCountries(): Flow<NetworkResource<List<Region>>>
    fun getAllRegionsInCountry(countryId: String): Flow<NetworkResource<List<Region>>>

    fun getAllIndustries(): Flow<NetworkResource<List<Industry>>>
}