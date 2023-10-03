package ru.practicum.android.diploma.domain.filter

import android.util.Log
import javax.inject.Inject

class FilterInteractorImpl @Inject constructor(
    private val repository: FilterRepository
) : FilterInteractor {
}