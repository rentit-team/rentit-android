package com.example.rentit.navigation.splash

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.rentit.presentation.splash.SplashRoute

fun NavGraphBuilder.splashGraph(navHostController: NavHostController) {
    composable<SplashRoute.Splash> { SplashRoute(navHostController) }
}