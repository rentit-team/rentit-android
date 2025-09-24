package com.example.rentit.presentation.rentaldetail.photobeforereturn

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.example.rentit.R
import com.example.rentit.common.component.layout.RentItLoadingScreen
import com.example.rentit.presentation.main.MainViewModel
import com.example.rentit.presentation.rentaldetail.components.rememberTakePhotoLauncher

@Composable
fun PhotoBeforeReturnRoute(navHostController: NavHostController, productId: Int, reservationId: Int) {
    val mainViewModel: MainViewModel = hiltViewModel()

    val viewModel: PhotoBeforeReturnViewModel = hiltViewModel()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val cameraLauncher = rememberTakePhotoLauncher { uri, file -> viewModel.onTakePhotoSuccess(uri, file) }

    LaunchedEffect(Unit) {
        viewModel.fetchBeforePhotoUrls(productId, reservationId)
        mainViewModel.setRetryAction { viewModel.retryFetchBeforePhotoUrls(productId, reservationId) }
    }

    LaunchedEffect(Unit) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.sideEffect.collect {
                when (it) {
                    PhotoBeforeReturnSideEffect.PopBackToRentalDetail -> {
                        navHostController.popBackStack()
                    }
                    PhotoBeforeReturnSideEffect.ToastUploadSuccess -> {
                        Toast.makeText(context, R.string.toast_photo_upload_success, Toast.LENGTH_SHORT).show()
                    }
                    PhotoBeforeReturnSideEffect.ToastUploadFailed -> {
                        Toast.makeText(context, R.string.toast_photo_upload_failed, Toast.LENGTH_SHORT).show()
                    }
                    is PhotoBeforeReturnSideEffect.CommonError -> {
                        mainViewModel.handleError(it.throwable)
                    }
                }
            }
        }
    }

    PhotoBeforeReturnScreen(
        currentPageNumber = uiState.currentPageNumber,
        requiredPhotoCnt = uiState.requiredPhotoCnt,
        takenPhotoCnt = uiState.takenPhotoCnt,
        beforePhotoUrl = uiState.currentBeforePhotoUrl,
        takenPhotoUri = uiState.takenPhotoUri,
        isLastPage = uiState.isLastPage,
        isRegisterAvailable = uiState.isRegisterAvailable,
        isNextAvailable = uiState.isNextAvailable,
        isBackAvailable = uiState.isBackAvailable,
        showFullImageDialog = uiState.showFullImageDialog,
        onFullImageDialogDismiss = viewModel::onFullImageDialogDismiss,
        onPageNext = viewModel::goToNextPage,
        onPageBack = viewModel::goToPreviousPage,
        onBeforeImageClick = viewModel::onBeforeImageClicked,
        onTakePhoto = cameraLauncher,
        onRegister = { viewModel.uploadPhotos(productId, reservationId) },
    )

    RentItLoadingScreen(uiState.isLoading || uiState.isUploadInProgress)
}