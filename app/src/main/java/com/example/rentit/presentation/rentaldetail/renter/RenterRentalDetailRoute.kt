package com.example.rentit.presentation.rentaldetail.renter

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.rememberScrollState
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
import com.example.rentit.navigation.rentaldetail.navigateToPhotoBeforeReturn
import com.example.rentit.navigation.rentaldetail.navigateToRentalPhotoCheck
import com.example.rentit.presentation.rentaldetail.dialog.RentalCancelDialog
import com.example.rentit.presentation.rentaldetail.dialog.TrackingRegistrationDialog
import com.example.rentit.presentation.rentaldetail.dialog.UnknownStatusDialog

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RenterRentalDetailRoute(navHostController: NavHostController, productId: Int, reservationId: Int) {

    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
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
                    RenterRentalDetailSideEffect.NavigateBack -> {
                        navHostController.popBackStack()
                    }
                    RenterRentalDetailSideEffect.NavigateToPay -> {
                        println("NavigateToPay")
                    }
                    RenterRentalDetailSideEffect.NavigateToPhotoBeforeReturn -> {
                        navHostController.navigateToPhotoBeforeReturn(productId, reservationId)
                    }
                    RenterRentalDetailSideEffect.NavigateToRentalPhotoCheck -> {
                        navHostController.navigateToRentalPhotoCheck(productId, reservationId)
                    }
                    RenterRentalDetailSideEffect.ToastErrorGetCourierNames -> {
                        Toast.makeText(context, context.getString(R.string.toast_error_get_courier_names), Toast.LENGTH_SHORT).show()
                    }
                    RenterRentalDetailSideEffect.ToastSuccessTrackingRegistration -> {
                        Toast.makeText(context, context.getString(R.string.toast_success_post_tracking_registration), Toast.LENGTH_SHORT).show()
                    }
                    RenterRentalDetailSideEffect.ToastErrorTrackingRegistration -> {
                        Toast.makeText(context, context.getString(R.string.toast_error_post_tracking_registration), Toast.LENGTH_SHORT).show()
                    }
                    RenterRentalDetailSideEffect.ToastCancelRentalSuccess -> {
                        Toast.makeText(context, context.getString(R.string.toast_cancel_rental_success), Toast.LENGTH_SHORT).show()
                    }
                    RenterRentalDetailSideEffect.ToastCancelRentalFailed -> {
                        Toast.makeText(context, context.getString(R.string.toast_cancel_rental_failed), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    RentalDetailRenterScreen(
        uiModel = rentalDetailUiModel,
        scrollState = scrollState,
        isLoading = uiState.isLoading,
        onBackPressed = viewModel::navigateBack,
        onPayClick = viewModel::navigateToPay,
        onCancelRentClick = viewModel::showCancelDialog,
        onPhotoTaskClick = viewModel::navigateToPhotoBeforeReturn,
        onTrackingNumTaskClick = viewModel::showTrackingRegDialog,
        onCheckPhotoClick = viewModel::navigateToRentalPhotoCheck
    )

    if(uiState.showCancelDialog){
        RentalCancelDialog(
            onClose = viewModel::dismissCancelDialog,
            onCancelAccept = { viewModel.confirmCancel(productId, reservationId) }
        )
    }

    if(uiState.showTrackingRegDialog && uiState.trackingCourierNames.isNotEmpty()){
        TrackingRegistrationDialog(
            courierNames = uiState.trackingCourierNames,
            selectedCourierName = uiState.selectedCourierName,
            trackingNumber = uiState.trackingNumber,
            showTrackingNumberEmptyError = uiState.showTrackingNumberEmptyError,
            onSelectCourier = viewModel::changeSelectedCourierName,
            onTrackingNumberChange = viewModel::changeTrackingNumber,
            onClose = viewModel::dismissTrackingRegDialog,
            onConfirm = { viewModel.confirmTrackingReg(productId, reservationId) }
        )
    }

    if(uiState.showUnknownStatusDialog){
        UnknownStatusDialog(viewModel::navigateBack)
    }
}