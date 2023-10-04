package ru.practicum.android.diploma.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.practicum.android.diploma.App
import ru.practicum.android.diploma.di.modules.DomainModule
import ru.practicum.android.diploma.di.modules.DataModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    DataModule::class,
    DomainModule::class,
    AppSubcomponents::class])
interface AppComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
    fun inject(app: App)
    fun activityComponent(): ActivityComponent.Factory
}