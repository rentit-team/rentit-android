package com.example.rentit.presentation.splash

sealed class SplashSideEffect {
    data object NavigateToMain: SplashSideEffect()
    data object NavigateToLogin: SplashSideEffect()
}