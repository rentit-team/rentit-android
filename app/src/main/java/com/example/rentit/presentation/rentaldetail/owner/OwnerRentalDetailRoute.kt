package com.example.rentit.presentation.rentaldetail.owner

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.example.rentit.presentation.rentaldetail.dialog.RentalCancelDialog
import com.example.rentit.presentation.rentaldetail.dialog.TrackingRegistrationDialog
import com.example.rentit.presentation.rentaldetail.dialog.UnknownStatusDialog

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OwnerRentalDetailRoute(navHostController: NavHostController, productId: Int, reservationId: Int) {

    val lifecycleOwner = LocalLifecycleOwner.current
    val viewModel: OwnerRentalDetailViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val rentalDetailUiModel by viewModel.rentalDetailUiModel.collectAsStateWithLifecycle()

    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.setLoading(true)
        viewModel.getRentalDetail(productId, reservationId)
        viewModel.setLoading(false)
    }

    LaunchedEffect(Unit) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.sideEffect.collect {
                when(it) {
                    is OwnerRentalDetailSideEffect.NavigateToPhotoBeforeRent -> { println("NavigateToPhotoBeforeRent") }
                    is OwnerRentalDetailSideEffect.NavigateToRentalPhotoCheck -> { println("NavigateToRentalPhotoCheck") }
                }
            }
        }
    }


    OwnerRentalDetailScreen(
        uiModel = rentalDetailUiModel,
        scrollState = scrollState,
        isLoading = true,
        onBackClick = { navHostController.popBackStack() },
        onRequestResponseClick = {},
        onCancelRentClick = viewModel::showCancelDialog,
        onPhotoTaskClick = viewModel::navigateToPhotoBeforeRent,
        onTrackingNumTaskClick = viewModel::showTrackingRegDialog,
        onCheckPhotoClick = viewModel::navigateToRentalPhotoCheck
    )

    if(uiState.showCancelDialog){
        RentalCancelDialog(
            onClose = viewModel::dismissCancelDialog,
            onCancelAccept = viewModel::confirmCancel
        )
    }

    if(uiState.showTrackingRegDialog){
        TrackingRegistrationDialog(
            companyList = emptyList(),
            selectedCompany = "",
            onSelectCompany = { },
            trackingNum = "",
            onTrackingNumChange = { },
            onClose = viewModel::dismissTrackingRegDialog,
            onConfirm = viewModel::confirmTrackingReg
        )
    }

    if(uiState.showUnknownStatusDialog){
        UnknownStatusDialog { navHostController.popBackStack() }
    }
}