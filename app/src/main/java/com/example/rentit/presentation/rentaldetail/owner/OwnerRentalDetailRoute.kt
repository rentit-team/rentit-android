package com.example.rentit.presentation.rentaldetail.owner

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
import com.example.rentit.common.component.dialog.RequestAcceptDialog
import com.example.rentit.navigation.rentaldetail.navigateToPhotoBeforeRent
import com.example.rentit.navigation.rentaldetail.navigateToRentalPhotoCheck
import com.example.rentit.presentation.rentaldetail.dialog.RentalCancelDialog
import com.example.rentit.presentation.rentaldetail.dialog.TrackingRegistrationDialog
import com.example.rentit.presentation.rentaldetail.dialog.UnknownStatusDialog

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OwnerRentalDetailRoute(navHostController: NavHostController, productId: Int, reservationId: Int) {

    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
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
                    OwnerRentalDetailSideEffect.NavigateBack -> {
                        navHostController.popBackStack()
                    }
                    OwnerRentalDetailSideEffect.NavigateToPhotoBeforeRent -> {
                        navHostController.navigateToPhotoBeforeRent(productId, reservationId)
                    }
                    OwnerRentalDetailSideEffect.NavigateToRentalPhotoCheck -> {
                        navHostController.navigateToRentalPhotoCheck(productId, reservationId)
                    }
                    OwnerRentalDetailSideEffect.ToastErrorGetCourierNames -> {
                        Toast.makeText(context, context.getString(R.string.toast_error_get_courier_names), Toast.LENGTH_SHORT).show()
                    }
                    OwnerRentalDetailSideEffect.ToastSuccessTrackingRegistration -> {
                        Toast.makeText(context, context.getString(R.string.toast_success_post_tracking_registration), Toast.LENGTH_SHORT).show()
                    }
                    OwnerRentalDetailSideEffect.ToastErrorTrackingRegistration -> {
                        Toast.makeText(context, context.getString(R.string.toast_error_post_tracking_registration), Toast.LENGTH_SHORT).show()
                    }
                    OwnerRentalDetailSideEffect.ToastCancelRentalSuccess -> {
                        Toast.makeText(context, context.getString(R.string.toast_cancel_rental_success), Toast.LENGTH_SHORT).show()
                    }
                    OwnerRentalDetailSideEffect.ToastCancelRentalFailed -> {
                        Toast.makeText(context, context.getString(R.string.toast_cancel_rental_failed), Toast.LENGTH_SHORT).show()
                    }
                    OwnerRentalDetailSideEffect.ToastAcceptRentalSuccess -> {
                        Toast.makeText(context, context.getString(R.string.toast_accept_rental_success), Toast.LENGTH_SHORT).show()
                    }
                    OwnerRentalDetailSideEffect.ToastAcceptRentalFailed -> {
                        Toast.makeText(context, context.getString(R.string.toast_accept_rental_failed), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


    OwnerRentalDetailScreen(
        uiModel = rentalDetailUiModel,
        scrollState = scrollState,
        isLoading = uiState.isLoading,
        onBackClick = viewModel::navigateBack,
        onRequestResponseClick = viewModel::showRequestAcceptDialog,
        onCancelRentClick = viewModel::showCancelDialog,
        onPhotoTaskClick = viewModel::navigateToPhotoBeforeRent,
        onTrackingNumTaskClick = viewModel::showTrackingRegDialog,
        onCheckPhotoClick = viewModel::navigateToRentalPhotoCheck
    )

    uiState.requestAcceptDialog?.let {
        RequestAcceptDialog(
            uiModel = it,
            onClose = viewModel::dismissRequestAcceptDialog,
            onAccept = { viewModel.acceptRequest(productId, reservationId) },
        )
    }

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