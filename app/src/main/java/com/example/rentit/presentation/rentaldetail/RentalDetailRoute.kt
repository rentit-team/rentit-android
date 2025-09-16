package com.example.rentit.presentation.rentaldetail

import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.example.rentit.R
import com.example.rentit.common.component.dialog.RequestAcceptDialog
import com.example.rentit.common.component.layout.LoadingScreen
import com.example.rentit.common.enums.RentalRole
import com.example.rentit.navigation.chatroom.navigateToChatRoom
import com.example.rentit.navigation.pay.navigateToPay
import com.example.rentit.navigation.productdetail.navigateToProductDetail
import com.example.rentit.navigation.rentaldetail.navigateToPhotoBeforeRent
import com.example.rentit.navigation.rentaldetail.navigateToPhotoBeforeReturn
import com.example.rentit.navigation.rentaldetail.navigateToRentalPhotoCheck
import com.example.rentit.presentation.main.MainViewModel
import com.example.rentit.presentation.rentaldetail.dialog.RentalCancelDialog
import com.example.rentit.presentation.rentaldetail.dialog.TrackingRegistrationDialog
import com.example.rentit.presentation.rentaldetail.dialog.TransactionReceiptConfirmDialog
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RentalDetailRoute(navHostController: NavHostController, productId: Int, reservationId: Int) {
    val backStackEntry = navHostController.currentBackStackEntry
    val mainViewModel: MainViewModel? = backStackEntry?.let { hiltViewModel(it) }

    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val viewModel: RentalDetailViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val scrollState = rememberScrollState()
    val pullToRefreshState = rememberPullToRefreshState()

    LaunchedEffect(Unit) {
        viewModel.getRentalDetail(productId, reservationId)
        mainViewModel?.setRetryAction { viewModel.reloadData(productId, reservationId) }
    }

    LaunchedEffect(Unit) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.sideEffect.collect {
                when(it) {
                    is RentalDetailSideEffect.DocumentLoadSuccess -> {
                        openPdf(context, it.file)
                    }
                    RentalDetailSideEffect.NavigateBack -> {
                        navHostController.popBackStack()
                    }
                    RentalDetailSideEffect.NavigateToProductDetail -> {
                        navHostController.navigateToProductDetail(productId)
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
                    is RentalDetailSideEffect.NavigateToChatRoom -> {
                        navHostController.navigateToChatRoom(it.chatRoomId)
                    }
                    RentalDetailSideEffect.ToastDocumentLoadFailed -> {
                        Toast.makeText(context, R.string.toast_load_transaction_receipt_failed, Toast.LENGTH_SHORT).show()
                    }
                    RentalDetailSideEffect.ToastErrorGetCourierNames -> {
                        Toast.makeText(context, R.string.toast_error_get_courier_names, Toast.LENGTH_SHORT).show()
                    }
                    RentalDetailSideEffect.ToastSuccessTrackingRegistration -> {
                        Toast.makeText(context, R.string.toast_success_post_tracking_registration, Toast.LENGTH_SHORT).show()
                    }
                    RentalDetailSideEffect.ToastErrorTrackingRegistration -> {
                        Toast.makeText(context, R.string.toast_error_post_tracking_registration, Toast.LENGTH_SHORT).show()
                    }
                    RentalDetailSideEffect.ToastCancelRentalSuccess -> {
                        Toast.makeText(context, R.string.toast_cancel_rental_success, Toast.LENGTH_SHORT).show()
                    }
                    RentalDetailSideEffect.ToastCancelRentalFailed -> {
                        Toast.makeText(context, R.string.toast_cancel_rental_failed, Toast.LENGTH_SHORT).show()
                    }
                    RentalDetailSideEffect.ToastAcceptRentalSuccess -> {
                        Toast.makeText(context, R.string.toast_accept_rental_success, Toast.LENGTH_SHORT).show()
                    }
                    RentalDetailSideEffect.ToastAcceptRentalFailed -> {
                        Toast.makeText(context, R.string.toast_accept_rental_failed, Toast.LENGTH_SHORT).show()
                    }
                    RentalDetailSideEffect.ToastAcceptedMessageSendSuccess -> {
                        Toast.makeText(context, R.string.toast_accepted_message_send_success, Toast.LENGTH_SHORT).show()
                    }
                    RentalDetailSideEffect.ToastChatRoomError -> {
                        Toast.makeText(context, R.string.toast_chat_room_error, Toast.LENGTH_SHORT).show()
                    }
                    is RentalDetailSideEffect.CommonError -> {
                        mainViewModel?.handleError(it.throwable)
                    }
                }
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.resetDialogState()
        }
    }

    when(uiState.role) {
        RentalRole.OWNER -> {
            OwnerRentalDetailScreen(
                uiModel = uiState.rentalDetailStatusModel,
                scrollState = scrollState,
                pullToRefreshState = pullToRefreshState,
                isRefreshing = uiState.isRefreshing,
                onRefresh = { viewModel.refreshData(productId, reservationId) },
                onBackClick = viewModel::navigateBack,
                onTransactionReceiptClick = viewModel::showTransactionReceiptConfirmDialog,
                onRequestResponseClick = viewModel::showRequestAcceptDialog,
                onCancelRentClick = viewModel::showCancelDialog,
                onPhotoTaskClick = viewModel::navigateToPhotoBeforeRent,
                onTrackingNumTaskClick = viewModel::showTrackingRegDialog,
                onCheckPhotoClick = viewModel::navigateToRentalPhotoCheck,
                onRentalSummaryClick = viewModel::navigateToProductDetail,
                onChattingClick = { viewModel.onChattingClicked(productId) },
            )
        }
        RentalRole.RENTER -> {
            RentalDetailRenterScreen(
                uiModel = uiState.rentalDetailStatusModel,
                scrollState = scrollState,
                pullToRefreshState = pullToRefreshState,
                isRefreshing = uiState.isRefreshing,
                onRefresh = { viewModel.refreshData(productId, reservationId) },
                onBackClick = viewModel::navigateBack,
                onTransactionReceiptClick = viewModel::showTransactionReceiptConfirmDialog,
                onPayClick = viewModel::navigateToPay,
                onCancelRentClick = viewModel::showCancelDialog,
                onPhotoTaskClick = viewModel::navigateToPhotoBeforeReturn,
                onTrackingNumTaskClick = viewModel::showTrackingRegDialog,
                onCheckPhotoClick = viewModel::navigateToRentalPhotoCheck,
                onRentalSummaryClick = viewModel::navigateToProductDetail,
                onChattingClick = { viewModel.onChattingClicked(productId) },
            )
        }
        RentalRole.DEFAULT -> { }
    }

    LoadingScreen(uiState.isLoading)

    if(uiState.showTransactionReceiptConfirmDialog) {
        TransactionReceiptConfirmDialog(
            onDismiss = viewModel::dismissTransactionReceiptConfirmDialog,
            onConfirmClick = { viewModel.fetchTransactionReceipt(context, productId, reservationId) },
        )
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
            onConfirm = { viewModel.confirmTrackingReg(uiState.trackingRegisterRequestType, productId, reservationId) }
        )
    }
}

fun openPdf(context: Context, file: File) {
    val uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        file
    )
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, "application/pdf")
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(intent)
}