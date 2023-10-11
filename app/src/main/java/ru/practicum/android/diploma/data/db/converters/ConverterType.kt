package ru.practicum.android.diploma.data.db.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ConverterType {

    @TypeConverter
    fun listToJson(value: List<String>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String?): List<String> {
        if (value == null || value.trim() == "null"){
            return listOf()
        }

        val typeToken = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, typeToken)
    }
}