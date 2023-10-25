package ru.practicum.android.diploma.presentation.search

sealed interface LoadingPageErrorStates{
    data object Default: LoadingPageErrorStates
    data object NoInternet: LoadingPageErrorStates
    data object ServerError: LoadingPageErrorStates
}