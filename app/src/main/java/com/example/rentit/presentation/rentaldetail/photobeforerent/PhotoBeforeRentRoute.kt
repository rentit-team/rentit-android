package com.example.rentit.presentation.rentaldetail.photobeforerent

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
import com.example.rentit.presentation.rentaldetail.components.rememberTakePhotoLauncher

@Composable
fun PhotoBeforeRentRoute(navHostController: NavHostController, productId: Int, reservationId: Int) {

    val viewModel: PhotoBeforeRentViewModel = hiltViewModel()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val takePhotoLauncher = rememberTakePhotoLauncher(viewModel::onTakePhotoSuccess)

    LaunchedEffect(Unit) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.sideEffect.collect { sideEffect ->
                when (sideEffect) {
                    PhotoBeforeRentSideEffect.PopBackToRentalDetail -> {
                        navHostController.popBackStack()
                    }
                    PhotoBeforeRentSideEffect.ToastUploadSuccess -> {
                        Toast.makeText(context, context.getString(R.string.toast_photo_upload_success), Toast.LENGTH_SHORT).show()
                    }
                    PhotoBeforeRentSideEffect.ToastUploadFailed -> {
                        Toast.makeText(context, context.getString(R.string.toast_photo_upload_failed), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    PhotoBeforeRentScreen(
        minPhotoCnt = uiState.minPhotoCnt,
        maxPhotoCnt = uiState.maxPhotoCnt,
        isRegisterEnabled = uiState.isRegisterEnabled,
        isMaxPhotoTaken = uiState.isMaxPhotoTaken,
        takenPhotoUris = uiState.takenPhotoUris,
        onTakePhoto = takePhotoLauncher,
        onRemovePhoto = viewModel::onRemovePhotoSuccess,
        onRegister = { viewModel.uploadPhotos(productId, reservationId) }
    )

    RentItLoadingScreen(uiState.isUploadInProgress)
}