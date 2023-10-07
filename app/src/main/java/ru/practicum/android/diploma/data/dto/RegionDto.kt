package ru.practicum.android.diploma.data.dto

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.domain.models.Region

data class RegionDto(
    val id: String,
    @SerializedName("parent_id")val parentId: String?,
    val name: String,
    val areas: List<Region>
)
