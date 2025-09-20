package com.example.rentit.presentation.rentaldetail.rentalphotocheck

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
import com.example.rentit.presentation.main.MainViewModel

@Composable
fun RentalPhotoCheckRoute(navHostController: NavHostController, productId: Int, reservationId: Int) {
    val mainViewModel: MainViewModel = hiltViewModel()

    val lifecycleOwner = LocalLifecycleOwner.current
    val viewModel: RentalPhotoCheckViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.fetchPhotoUrls(productId, reservationId)
        mainViewModel.setRetryAction { viewModel.retryFetchPhotoUrls(productId, reservationId) }
    }

    LaunchedEffect(Unit) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.sideEffect.collect {
                if(it is RentalPhotoCheckSideEffect.CommonError) {
                    mainViewModel.handleError(it.throwable)
                }
            }
        }
    }

    RentalPhotoCheckScreen(
        totalPageCnt = uiState.totalPhotoCnt,
        currentPageNumber = uiState.currentPageNumber,
        isNextAvailable = uiState.isNextAvailable,
        isBackAvailable = uiState.isBackAvailable,
        beforePhotoUrl = uiState.beforePhotoUrl,
        afterPhotoUrl = uiState.afterPhotoUrl,
        previewPhotoUrl = uiState.previewPhotoUrl,
        onPhotoClick = viewModel::changePreviewPhotoUrl,
        onPageNext = viewModel::goToNextPage,
        onPageBack = viewModel::goToPreviousPage,
        onBackPressed = navHostController::popBackStack,
    )

    LoadingScreen(uiState.isLoading)
}