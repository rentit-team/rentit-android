package com.example.rentit.presentation.rentaldetail

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
import com.example.rentit.common.enums.RentalRole
import com.example.rentit.navigation.pay.navigateToPay
import com.example.rentit.navigation.rentaldetail.navigateToPhotoBeforeRent
import com.example.rentit.navigation.rentaldetail.navigateToPhotoBeforeReturn
import com.example.rentit.navigation.rentaldetail.navigateToRentalPhotoCheck
import com.example.rentit.presentation.rentaldetail.dialog.RentalCancelDialog
import com.example.rentit.presentation.rentaldetail.dialog.TrackingRegistrationDialog
import com.example.rentit.presentation.rentaldetail.dialog.UnknownStatusDialog

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RentalDetailRoute(navHostController: NavHostController, productId: Int, reservationId: Int) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val viewModel: RentalDetailViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

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
                    RentalDetailSideEffect.NavigateBack -> {
                        navHostController.popBackStack()
                    }
                    RentalDetailSideEffect.NavigateToPay -> {
                        navHostController.navigateToPay(productId, reservationId)
                    }
                    RentalDetailSideEffect.NavigateToPhotoBeforeRent -> {
                        navHostController.navigateToPhotoBeforeRent(productId, reservationId)
                    }
                    RentalDetailSideEffect.NavigateToPhotoBeforeReturn -> {
                        navHostController.navigateToPhotoBeforeReturn(productId, reservationId)
                    }
                    RentalDetailSideEffect.NavigateToRentalPhotoCheck -> {
                        navHostController.navigateToRentalPhotoCheck(productId, reservationId)
                    }
                    RentalDetailSideEffect.ToastErrorGetCourierNames -> {
                        Toast.makeText(context, context.getString(R.string.toast_error_get_courier_names), Toast.LENGTH_SHORT).show()
                    }
                    RentalDetailSideEffect.ToastSuccessTrackingRegistration -> {
                        Toast.makeText(context, context.getString(R.string.toast_success_post_tracking_registration), Toast.LENGTH_SHORT).show()
                    }
                    RentalDetailSideEffect.ToastErrorTrackingRegistration -> {
                        Toast.makeText(context, context.getString(R.string.toast_error_post_tracking_registration), Toast.LENGTH_SHORT).show()
                    }
                    RentalDetailSideEffect.ToastCancelRentalSuccess -> {
                        Toast.makeText(context, context.getString(R.string.toast_cancel_rental_success), Toast.LENGTH_SHORT).show()
                    }
                    RentalDetailSideEffect.ToastCancelRentalFailed -> {
                        Toast.makeText(context, context.getString(R.string.toast_cancel_rental_failed), Toast.LENGTH_SHORT).show()
                    }
                    RentalDetailSideEffect.ToastAcceptRentalSuccess -> {
                        Toast.makeText(context, context.getString(R.string.toast_accept_rental_success), Toast.LENGTH_SHORT).show()
                    }
                    RentalDetailSideEffect.ToastAcceptRentalFailed -> {
                        Toast.makeText(context, context.getString(R.string.toast_accept_rental_failed), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    when(uiState.role) {
        RentalRole.OWNER -> {
            OwnerRentalDetailScreen(
                uiModel = uiState.rentalDetailStatusModel,
                scrollState = scrollState,
                isLoading = uiState.isLoading,
                onBackClick = viewModel::navigateBack,
                onRequestResponseClick = viewModel::showRequestAcceptDialog,
                onCancelRentClick = viewModel::showCancelDialog,
                onPhotoTaskClick = viewModel::navigateToPhotoBeforeRent,
                onTrackingNumTaskClick = viewModel::showTrackingRegDialog,
                onCheckPhotoClick = viewModel::navigateToRentalPhotoCheck
            )
        }
        RentalRole.RENTER -> {
            RentalDetailRenterScreen(
                uiModel = uiState.rentalDetailStatusModel,
                scrollState = scrollState,
                isLoading = uiState.isLoading,
                onBackPressed = viewModel::navigateBack,
                onPayClick = viewModel::navigateToPay,
                onCancelRentClick = viewModel::showCancelDialog,
                onPhotoTaskClick = viewModel::navigateToPhotoBeforeReturn,
                onTrackingNumTaskClick = viewModel::showTrackingRegDialog,
                onCheckPhotoClick = viewModel::navigateToRentalPhotoCheck,
            )
        }
        RentalRole.DEFAULT -> { }
    }

    uiState.requestAcceptDialog?.let {
        RequestAcceptDialog(
            uiModel = it,
            onDismiss = viewModel::dismissRequestAcceptDialog,
            onAccept = { viewModel.acceptRequest(productId, reservationId) },
        )
    }

    if (uiState.showCancelDialog) {
        RentalCancelDialog(
            onDismiss = viewModel::dismissCancelDialog,
            onCancelAccept = { viewModel.confirmCancel(productId, reservationId) }
        )
    }

    if (uiState.showTrackingRegDialog && uiState.trackingCourierNames.isNotEmpty()) {
        TrackingRegistrationDialog(
            courierNames = uiState.trackingCourierNames,
            selectedCourierName = uiState.selectedCourierName,
            trackingNumber = uiState.trackingNumber,
            showTrackingNumberEmptyError = uiState.showTrackingNumberEmptyError,
            onSelectCourier = viewModel::changeSelectedCourierName,
            onTrackingNumberChange = viewModel::changeTrackingNumber,
            onDismiss = viewModel::dismissTrackingRegDialog,
            onConfirm = { viewModel.confirmTrackingReg(productId, reservationId) }
        )
    }

    if (uiState.showUnknownStatusDialog) {
        UnknownStatusDialog(viewModel::navigateBack)
    }
}