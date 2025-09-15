package com.example.rentit.presentation.main

sealed class MainSideEffect {
    data object NavigateToLogin: MainSideEffect()
}