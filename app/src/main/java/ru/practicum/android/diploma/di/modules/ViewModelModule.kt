package ru.practicum.android.diploma.di.modules

import dagger.Module
import dagger.Provides
import ru.practicum.android.diploma.domain.details.DetailsInteractor
import ru.practicum.android.diploma.domain.developers.DevelopersInteractor
import ru.practicum.android.diploma.domain.favourite.FavouriteInteractor
import ru.practicum.android.diploma.domain.filter.FilterInteractor
import ru.practicum.android.diploma.domain.search.SearchInteractor
import ru.practicum.android.diploma.domain.share.impl.SharingInteractorImpl
import ru.practicum.android.diploma.domain.similar.SimilarInteractor
import ru.practicum.android.diploma.presentation.details.view_model.DetailsViewModel
import ru.practicum.android.diploma.presentation.developers.view_model.DevelopersViewModel
import ru.practicum.android.diploma.presentation.favourite.view_model.FavouriteViewModel
import ru.practicum.android.diploma.presentation.filter.view_model.FilterViewModel
import ru.practicum.android.diploma.presentation.search.view_model.SearchViewModel
import ru.practicum.android.diploma.presentation.similar.view_model.SimilarViewModel

@Module
class ViewModelModule {
    @Provides
    fun provideDetailsViewModel(
        interactor: DetailsInteractor,
        sharingInteractor: SharingInteractorImpl,
    ): DetailsViewModel {
        return DetailsViewModel(interactor, sharingInteractor)
    }
    @Provides
    fun provideDevelopersViewModel(interactor: DevelopersInteractor): DevelopersViewModel {
        return DevelopersViewModel(interactor)
    }
    @Provides
    fun provideFavouriteViewModel(interactor: FavouriteInteractor): FavouriteViewModel {
        return FavouriteViewModel(interactor)
    }
    @Provides
    fun provideFilterViewModel(interactor: FilterInteractor): FilterViewModel {
        return FilterViewModel(interactor)
    }
    @Provides
    fun provideSearchViewModel(interactor: SearchInteractor, filterInteractor: FilterInteractor): SearchViewModel {
        return SearchViewModel(interactor, filterInteractor)
    }
    @Provides
    fun provideSimilarViewModel(interactor: SimilarInteractor): SimilarViewModel {
        return SimilarViewModel(interactor)
    }
}