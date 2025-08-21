package com.example.rentit.presentation.rentaldetail.owner.photobeforerent

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.rentit.presentation.rentaldetail.components.rememberTakePhotoLauncher

@Composable
fun PhotoBeforeRentRoute() {
    val viewModel: PhotoBeforeRentViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val takePhotoLauncher = rememberTakePhotoLauncher(viewModel::onTakePhotoSuccess)

    PhotoBeforeRentScreen(
        minPhotoCnt = uiState.minPhotoCnt,
        maxPhotoCnt = uiState.maxPhotoCnt,
        isRegisterEnabled = uiState.isRegisterEnabled,
        isMaxPhotoTaken = uiState.isMaxPhotoTaken,
        takenPhotoUris = uiState.takenPhotoUris,
        onTakePhoto = { takePhotoLauncher() },
        onRemovePhoto = viewModel::onRemovePhotoSuccess,
    )
}