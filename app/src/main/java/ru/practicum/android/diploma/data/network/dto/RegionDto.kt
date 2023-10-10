package ru.practicum.android.diploma.data.network.dto

import com.google.gson.annotations.SerializedName

data class RegionDto(
    val id: String,
    @SerializedName("parent_id")val parentId: String?,
    val name: String,
    val areas: List<RegionDto>
)
