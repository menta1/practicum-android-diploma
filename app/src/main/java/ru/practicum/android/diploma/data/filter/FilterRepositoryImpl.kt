package ru.practicum.android.diploma.data.filter

import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.converters.FiltersNetworkConverter
import ru.practicum.android.diploma.data.network.dto.IndustryDto
import ru.practicum.android.diploma.data.network.dto.IndustryResponse
import ru.practicum.android.diploma.data.network.dto.RegionDto
import ru.practicum.android.diploma.data.network.dto.RegionResponse
import ru.practicum.android.diploma.domain.filter.FilterRepository
import ru.practicum.android.diploma.domain.models.Filter
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.models.Region
import ru.practicum.android.diploma.util.NetworkResource
import javax.inject.Inject

class FilterRepositoryImpl @Inject constructor(
    private val converter: FiltersNetworkConverter,
    private val networkClient: NetworkClient,
    private val filterStorage: FilterStorage
) : FilterRepository {


    override fun getAllCountries(): Flow<NetworkResource<List<Region>>> = flow {

        val resultFromData = networkClient.getAllCountries()
        when (resultFromData.resultCode) {

            200 -> {
                emit(
                    NetworkResource(
                        data =
                        (resultFromData as RegionResponse).results.map { regionDto ->
                            converter.convertRegionToDomain(regionDto)
                        }, code = resultFromData.resultCode
                    )
                )
            }

            else -> {
                emit(NetworkResource(code = resultFromData.resultCode))
            }
        }

    }.flowOn(Dispatchers.IO)

    override fun getAllRegionsInCountry(countryId: String): Flow<NetworkResource<List<Region>>> =
        flow {

            val resultFromData = networkClient.getAllRegionsInCountry(countryId)
            when (resultFromData.resultCode) {

                200 -> {
                    val rawResults = (resultFromData as RegionResponse).results
                    val finalResults = mutableListOf<RegionDto>()

                    rawResults.first().areas.forEach { region ->
                        finalResults.add(region)
                        if (region.areas.isNotEmpty()) {
                            region.areas.forEach { town ->
                                finalResults.add(town)
                            }
                        }
                    }

                    emit(NetworkResource(data =
                    finalResults.sortedBy { it.name }.map { regionDto ->
                        converter.convertRegionToDomain(regionDto)
                    },
                        code = resultFromData.resultCode
                    )
                    )
                }

                else -> {
                    emit(NetworkResource(code = resultFromData.resultCode))
                }
            }

        }.flowOn(Dispatchers.IO)


    override fun getAllIndustries(): Flow<NetworkResource<List<Industry>>> = flow {

        val resultFromData = networkClient.getAllIndustries()

        when (resultFromData.resultCode) {


            200 -> {
                val rawResults = (resultFromData as IndustryResponse).results
                val finalResults = mutableListOf<IndustryDto>()

                rawResults.forEach { generalIndustry ->
                    finalResults.add(generalIndustry)
                    if (generalIndustry.industries != null) {
                        generalIndustry.industries.forEach { specificIndustry ->
                            finalResults.add(specificIndustry)
                        }
                    }
                }

                emit(NetworkResource(data =
                finalResults.sortedBy { it.name }.map { industryDto ->
                    converter.convertIndustryToDomain(industryDto)
                },
                    code = resultFromData.resultCode
                )
                )

            }

            else -> {
                emit(NetworkResource(code = resultFromData.resultCode))
            }
        }

    }.flowOn(Dispatchers.IO)


    override fun getFilter(): Filter? {
        val resultFromData = filterStorage.getFilter()
        return if (resultFromData == "") {
            null
        } else {
            Gson().fromJson(resultFromData, Filter::class.java)
        }
    }

    override fun editCountryNameAndId(country: Region) {
        val filterFromData = getFilter()

        val editedFilter =
            filterFromData?.copy(countryName = country.name, regionId = country.id)
                ?: Filter(countryName = country.name, regionId = country.id)

        filterStorage.editFilter(Gson().toJson(editedFilter))
    }

    override fun editRegionNameAndId(region: Region) {
        val filterFromData = getFilter()

        val editedFilter =
            filterFromData?.copy(regionName = region.name, regionId = region.id)
                ?: Filter(regionName = region.name, regionId = region.id)
        filterStorage.editFilter(Gson().toJson(editedFilter))
    }

    override fun editIndustryNameAndId(industry: Industry) {
        val filterFromData = getFilter()

        val editedFilter =
            filterFromData?.copy(industryId = industry.id, industryName = industry.name)
                ?: Filter(industryId = industry.id, industryName = industry.name)
        filterStorage.editFilter(Gson().toJson(editedFilter))
    }

    override fun editExpectedSalary(expectedSalary: Int) {
        val filterFromData = getFilter()

        val editedFilter =
            filterFromData?.copy(expectedSalary = expectedSalary) ?: Filter(expectedSalary = expectedSalary)
        filterStorage.editFilter(Gson().toJson(editedFilter))
    }

    override fun editIsOnlyWithSalary(isOnlyWithSalary: Boolean) {
        val filterFromData = getFilter()

        val editedFilter =
            filterFromData?.copy(isOnlyWithSalary = isOnlyWithSalary)
                ?: Filter(isOnlyWithSalary = isOnlyWithSalary)
        filterStorage.editFilter(Gson().toJson(editedFilter))
    }

    override fun clearCountryNameAndId() {
        val filterFromData = getFilter()

        val editedFilter =
            filterFromData?.copy(countryName = null, regionId = null, regionName = null)
        filterStorage.editFilter(Gson().toJson(editedFilter))

    }

    override fun clearRegionNameAndId() {
        val filterFromData = getFilter()

        val editedFilter = filterFromData?.copy(regionName = null, regionId = null)
        filterStorage.editFilter(Gson().toJson(editedFilter))

    }

    override fun clearIndustryNameAndId() {
        val filterFromData = getFilter()

        val editedFilter = filterFromData?.copy(industryName = null, industryId = null)
        filterStorage.editFilter(Gson().toJson(editedFilter))

    }

    override fun clearExpectedSalary() {
        val filterFromData = getFilter()

        val editedFilter = filterFromData?.copy(expectedSalary = null)
        filterStorage.editFilter(Gson().toJson(editedFilter))

    }

    override fun clearFilter() {
        filterStorage.clearFilter()
    }

    override fun isFilterEmpty(): Boolean = getFilter() == null

}
