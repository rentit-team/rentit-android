package com.example.rentit.presentation.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.example.rentit.common.component.layout.LoadingScreen
import com.example.rentit.navigation.auth.navigateToLogin
import com.example.rentit.navigation.bottomtab.navigateToHome
import com.example.rentit.presentation.main.MainViewModel

@Composable
fun SplashRoute(navHostController: NavHostController) {
    val mainViewModel: MainViewModel = hiltViewModel()

    val viewModel: SplashViewModel = hiltViewModel()
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        mainViewModel.setRetryAction(viewModel::retryCheckUserSession)
    }

    LaunchedEffect(Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.splashSideEffect.collect {
                when (it) {
                    SplashSideEffect.NavigateToMain -> navHostController.navigateToHome()
                    SplashSideEffect.NavigateToLogin -> navHostController.navigateToLogin()
                    is SplashSideEffect.CommonError -> {
                       mainViewModel.handleError(it.throwable)
                    }
                }
            }
        }
    }

    LoadingScreen(isLoading)
}