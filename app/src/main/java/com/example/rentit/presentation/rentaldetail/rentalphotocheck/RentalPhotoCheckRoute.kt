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
import com.example.rentit.presentation.rentaldetail.dialog.PhotoLoadFailedDialog

@Composable
fun RentalPhotoCheckRoute(navHostController: NavHostController, productId: Int, reservationId: Int) {

    val lifecycleOwner = LocalLifecycleOwner.current
    val viewModel: RentalPhotoCheckViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.fetchBeforePhotoUrls(productId, reservationId)
    }

    LaunchedEffect(Unit) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.sideEffect.collect { sideEffect ->
                when (sideEffect) {
                    RentalPhotoCheckSideEffect.PopBackToRentalDetail -> navHostController.popBackStack()
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
        onBackPressed = viewModel::navigateBack,
    )

    if(uiState.showFailedPhotoLoadDialog)
        PhotoLoadFailedDialog(viewModel::closeAndNavigateBack)
}