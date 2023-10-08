package ru.practicum.android.diploma.data.network.converters

import ru.practicum.android.diploma.data.network.dto.IndustryDto
import ru.practicum.android.diploma.data.network.dto.RegionDto
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.models.Region
import javax.inject.Inject

class FiltersNetworkConverter @Inject constructor() {

    fun convertRegionToDomain(regionDto: RegionDto): Region {
        with(regionDto) {
            return Region(
                id = id,
                parentId = parentId,
                name = name,
                areas = areas.map { convertRegionToDomain(it) }
            )
        }
    }

    fun convertIndustryToDomain(industryDto: IndustryDto): Industry {
        with(industryDto) {
            return Industry(
                id = id,
                name = name,
                industries = industries?.map { convertIndustryToDomain(it) }
            )
        }
    }
}