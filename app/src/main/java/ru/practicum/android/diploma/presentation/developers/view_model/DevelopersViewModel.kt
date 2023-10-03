package ru.practicum.android.diploma.presentation.developers.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.domain.developers.DevelopersInteractor
import javax.inject.Inject

class DevelopersViewModel @Inject constructor(val interactor: DevelopersInteractor): ViewModel() {

    fun testFun() {
        Log.d("TestFun", "DevelopersViewModel")
        interactor.testFun()
    }

}