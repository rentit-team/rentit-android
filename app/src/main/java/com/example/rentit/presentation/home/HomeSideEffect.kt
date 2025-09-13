package com.example.rentit.presentation.home

sealed class HomeSideEffect {
    data object ScrollToTop: HomeSideEffect()
    data class CommonError(val throwable: Throwable): HomeSideEffect()
}