package ru.practicum.android.diploma.data.db.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import ru.practicum.android.diploma.domain.models.Phone

class ConverterType {

    @TypeConverter
    fun listToJson(value: List<String>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = if (value.isEmpty()) {
        listOf()
    } else {
        Gson().fromJson(value, Array<String>::class.java).toList()
    }

    @TypeConverter
    fun phonesListToJson(value: List<Phone>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToPhonesList(value: String) = if (value.isEmpty()) {
        listOf()
    } else {
        Gson().fromJson(value, Array<Phone>::class.java).toList()
    }
}