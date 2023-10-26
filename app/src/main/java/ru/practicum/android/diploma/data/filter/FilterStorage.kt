package ru.practicum.android.diploma.data.filter

interface FilterStorage {

    fun getFilter(): String

    fun editFilter(editedFilter: String)

    fun clearFilter()

    fun getPreviousCountry(): String

    fun editPreviousCountry(countryId: String)

    fun putSearchMode(isSearchingNow: Boolean)

    fun getSearchingMode(): Boolean
}