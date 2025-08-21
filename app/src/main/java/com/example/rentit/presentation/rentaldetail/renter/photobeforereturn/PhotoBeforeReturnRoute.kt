package com.example.rentit.presentation.rentaldetail.renter.photobeforereturn

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
import com.example.rentit.R
import com.example.rentit.common.component.layout.LoadingScreen
import com.example.rentit.presentation.rentaldetail.components.rememberTakePhotoLauncher
import com.example.rentit.presentation.rentaldetail.dialog.PhotoLoadFailedDialog

@Composable
fun PhotoBeforeReturnRoute(productId: Int, reservationId: Int) {

    val viewModel: PhotoBeforeReturnViewModel = hiltViewModel()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val cameraLauncher = rememberTakePhotoLauncher { uri, file -> viewModel.onTakePhotoSuccess(uri, file) }

    LaunchedEffect(Unit) {
        viewModel.fetchBeforePhotoUrls(productId, reservationId)
    }

    LaunchedEffect(Unit) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.sideEffect.collect { sideEffect ->
                when (sideEffect) {
                    PhotoBeforeReturnSideEffect.PopBackToRentalDetail -> { println("pop back to rental detail") }
                    PhotoBeforeReturnSideEffect.ToastUploadSuccess -> {
                        Toast.makeText(context, context.getString(R.string.toast_photo_upload_success), Toast.LENGTH_SHORT).show()
                    }
                    PhotoBeforeReturnSideEffect.ToastUploadFailed -> {
                        Toast.makeText(context, context.getString(R.string.toast_photo_upload_failed), Toast.LENGTH_SHORT).show()
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
        onPageNext = viewModel::goToNextPage,
        onPageBack = viewModel::goToPreviousPage,
        onTakePhoto = cameraLauncher,
        onRegister = { viewModel.uploadPhotos(productId, reservationId) },
    )

    LoadingScreen(uiState.isUploadInProgress)

    if(uiState.showFailedPhotoLoadDialog)
        PhotoLoadFailedDialog(viewModel::closeAndNavigateBack)
}