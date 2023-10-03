package ru.practicum.android.diploma.domain.developers

import android.util.Log
import javax.inject.Inject

class DevelopersInteractorImpl @Inject constructor(
    private val repository: DevelopersRepository
): DevelopersInteractor {
    override fun testFun() {
        Log.d("TestFun", "DevelopersInteractorImpl")
        repository.testFun()
    }
}