package ru.practicum.android.diploma.di

import dagger.Subcomponent
import ru.practicum.android.diploma.di.modules.ViewModelModule
import ru.practicum.android.diploma.presentation.details.ui.DetailsFragment
import ru.practicum.android.diploma.presentation.developers.ui.DevelopersFragment
import ru.practicum.android.diploma.presentation.favourite.ui.FavouriteFragment
import ru.practicum.android.diploma.presentation.filter.ui.FilterCountryFragment
import ru.practicum.android.diploma.presentation.filter.ui.FilterPlaceFragment
import ru.practicum.android.diploma.presentation.filter.ui.FilterRegionFragment
import ru.practicum.android.diploma.presentation.filter.ui.FilterSectorFragment
import ru.practicum.android.diploma.presentation.filter.ui.FilterSettingsFragment
import ru.practicum.android.diploma.presentation.root.ui.RootActivity
import ru.practicum.android.diploma.presentation.search.ui.SearchFragment
import ru.practicum.android.diploma.presentation.similar.ui.SimilarFragment


@Subcomponent(modules = [ViewModelModule::class])
interface ActivityComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): ActivityComponent
    }

    fun inject(rootActivity: RootActivity)
    fun inject(searchFragment: SearchFragment)
    fun inject(favouriteFragment: FavouriteFragment)
    fun inject(developersFragment: DevelopersFragment)
    fun inject(filterSettingsFragment: FilterSettingsFragment)
    fun inject(filterCountryFragment: FilterCountryFragment)
    fun inject(filterPlaceFragment: FilterPlaceFragment)
    fun inject(filterSectorFragment: FilterSectorFragment)
    fun inject(filterRegionFragment: FilterRegionFragment)
    fun inject(similarFragment: SimilarFragment)
    fun inject(detailsFragment: DetailsFragment)

}