package com.example.rentit.presentation.rentaldetail.renter

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
fun RenterRentalDetailRoute(navHostController: NavHostController, productId: Int, reservationId: Int) {

    val lifecycleOwner = LocalLifecycleOwner.current
    val viewModel: RenterRentalDetailViewModel = hiltViewModel()
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
                    is RenterRentalDetailSideEffect.NavigateToPay -> { println("NavigateToPay") }
                    is RenterRentalDetailSideEffect.NavigateToPhotoBeforeReturn -> { println("NavigateToPhotoBeforeReturn") }
                    is RenterRentalDetailSideEffect.NavigateToRentalPhotoCheck -> { println("NavigateToRentalPhotoCheck") }
                }
            }
        }
    }

    RentalDetailRenterScreen(
        uiModel = rentalDetailUiModel,
        scrollState = scrollState,
        isLoading = uiState.isLoading,
        onBackPressed = { navHostController.popBackStack() },
        onPayClick = viewModel::navigateToPay,
        onCancelRentClick = viewModel::showCancelDialog,
        onTrackingNumTaskClick = viewModel::showTrackingRegDialog,
        onPhotoTaskClick = viewModel::navigateToPhotoBeforeReturn,
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
            courierNames = emptyList(),
            selectedCourierName = "",
            onSelectCourier = { },
            trackingNumber = "",
            onTrackingNumberChange = { },
            onClose = viewModel::dismissTrackingRegDialog,
            onConfirm = viewModel::confirmTrackingReg
        )
    }

    if(uiState.showUnknownStatusDialog){
        UnknownStatusDialog { navHostController.popBackStack() }
    }
}