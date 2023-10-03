package ru.practicum.android.diploma.data.developers

import android.util.Log
import ru.practicum.android.diploma.domain.developers.DevelopersRepository
import javax.inject.Inject

class DevelopersRepositoryImpl @Inject constructor(): DevelopersRepository {
    override fun testFun() {
        Log.d("TestFun", "DevelopersRepositoryImpl")
    }
}