package ru.practicum.android.diploma.data.filter

import android.util.Log
import ru.practicum.android.diploma.data.network.converters.FiltersNetworkConverter
import ru.practicum.android.diploma.domain.filter.FilterRepository
import javax.inject.Inject

class FilterRepositoryImpl @Inject constructor(converter: FiltersNetworkConverter): FilterRepository {
}