package ru.practicum.android.diploma.data.network.dto

import com.google.gson.annotations.SerializedName

class VacancyResponse(
    @SerializedName("items") val results: ArrayList<VacancyDto>,
    @SerializedName("page") val page: Int?,
    @SerializedName("pages") val pages: Int?,
    @SerializedName("found") val found: Int?
) : Response()