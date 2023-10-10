package ru.practicum.android.diploma.data.db.converters

import androidx.room.TypeConverter
import com.google.gson.Gson

class ConverterType {

    @TypeConverter
    fun listToJson(value: List<String>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = if (value.isEmpty()) {
        listOf()
    } else {
        Gson().fromJson(value, Array<String>::class.java).toList()
    }
}