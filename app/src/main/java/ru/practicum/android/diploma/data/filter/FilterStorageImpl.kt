package ru.practicum.android.diploma.data.filter

import android.content.Context
import android.content.Context.MODE_PRIVATE
import javax.inject.Inject

class FilterStorageImpl @Inject constructor(context: Context) : FilterStorage {

    private val sharedPrefs = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)


    override fun getFilter(): String {
        return sharedPrefs.getString(FILTER, "") ?: ""
    }

    override fun editFilter(editedFilter: String) {
        sharedPrefs.edit().putString(FILTER, editedFilter).apply()
    }

    override fun clearFilter() {
        sharedPrefs.edit().putString(FILTER, "").apply()
    }

    override fun getPreviousCountry(): String {
        return sharedPrefs.getString(PREVIOUS_COUNTRY, "") ?: ""
    }

    override fun editPreviousCountry(countryId: String) {
        sharedPrefs.edit().putString(PREVIOUS_COUNTRY, countryId).apply()
    }

    override fun getSavedInput(): String {
        return sharedPrefs.getString(SAVED_INPUT, "") ?: ""
    }

    override fun putSavedInput(input: String) {
        sharedPrefs.edit().putString(SAVED_INPUT, input).apply()
    }

    override fun clearSavedInput() {
        sharedPrefs.edit().putString(SAVED_INPUT, "").apply()
    }

    companion object {
        private const val SHARED_PREFS = "shared_prefs"
        private const val FILTER = "filter"
        private const val PREVIOUS_COUNTRY = "prev_country"
        private const val SAVED_INPUT = "saved_input"
    }
}