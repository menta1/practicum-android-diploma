package ru.practicum.android.diploma

import android.app.Application
import ru.practicum.android.diploma.di.AppComponent
import ru.practicum.android.diploma.di.DaggerAppComponent

class App: Application() {


    val appComponent: AppComponent by lazy {
        initializeComponent()
    }

    open fun initializeComponent(): AppComponent {
        return DaggerAppComponent.factory().create(this)
    }

}