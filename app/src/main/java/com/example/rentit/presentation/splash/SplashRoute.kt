package com.example.rentit.presentation.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.example.rentit.navigation.auth.navigateToLogin
import com.example.rentit.navigation.bottomtab.navigateToHome

@Composable
fun SplashRoute(navHostController: NavHostController) {
    val viewModel: SplashViewModel = hiltViewModel()
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.splashSideEffect.collect {
                when (it) {
                    SplashSideEffect.NavigateToMain -> navHostController.navigateToHome()
                    SplashSideEffect.NavigateToLogin -> navHostController.navigateToLogin()
                }
            }
        }
    }

    SplashScreen(
        isLoading = uiState.value.isLoading,
        showErrorDialog = uiState.value.showErrorDialog,
        onRetry = viewModel::retryCheckUserSession
    )
}