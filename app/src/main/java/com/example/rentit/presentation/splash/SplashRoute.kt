package com.example.rentit.presentation.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.rentit.navigation.auth.navigateToLogin
import com.example.rentit.navigation.bottomtab.navigateToHome

@Composable
fun SplashRoute(navHostController: NavHostController) {
    val splashViewModel: SplashViewModel = hiltViewModel()

    LaunchedEffect(Unit) {
        splashViewModel.splashSideEffect.collect {
            when (it) {
                SplashSideEffect.NavigateToMain -> navHostController.navigateToHome()
                SplashSideEffect.NavigateToLogin -> navHostController.navigateToLogin()
            }
        }
    }
}