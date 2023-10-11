package ru.practicum.android.diploma.data.filter

import android.content.Context
import android.content.Context.MODE_PRIVATE
import javax.inject.Inject

class FilterStorageImpl @Inject constructor(context: Context): FilterStorage {

    private val sharedPrefs = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)


    override fun getFilter(): String {
        return sharedPrefs.getString(FILTER, "") ?: ""
    }

    override fun editFilter(editedFilter: String) {
        sharedPrefs.edit().putString(FILTER,editedFilter).apply()
    }

    override fun clearFilter() {
        sharedPrefs.edit().putString(FILTER,"").apply()
    }

    companion object{
        private const val SHARED_PREFS = "shared_prefs"
        private const val FILTER = "filter"
    }
}