package com.example.rentit.navigation.splash

import kotlinx.serialization.Serializable

sealed class SplashRoute {
    @Serializable
    data object Splash : SplashRoute()
}