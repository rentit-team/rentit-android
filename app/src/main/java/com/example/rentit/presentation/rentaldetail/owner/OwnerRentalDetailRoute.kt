package com.example.rentit.presentation.rentaldetail.owner

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OwnerRentalDetailRoute(navHostController: NavHostController) {

    val viewModel: OwnerRentalDetailViewModel = hiltViewModel()
    val uiModel by viewModel.uiModel.collectAsStateWithLifecycle()

    val scrollState = rememberScrollState()

    OwnerRentalDetailScreen(
        uiModel = uiModel,
        scrollState = scrollState,
        isLoading = true,
        onBackClick = { navHostController.popBackStack() },
        onRequestResponseClick = { },
        onCancelRentClick = { },
        onPhotoTaskClick = { },
        onTrackingNumTaskClick = { },
        onCheckPhotoClick = { }
    )
}