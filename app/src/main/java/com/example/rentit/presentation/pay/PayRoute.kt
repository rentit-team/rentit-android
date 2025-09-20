package com.example.rentit.presentation.pay

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
import com.example.rentit.presentation.main.MainViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PayRoute(navHostController: NavHostController, productId: Int, reservationId: Int) {
    val mainViewModel: MainViewModel = hiltViewModel()

    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val viewModel: PayViewModel = hiltViewModel()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.getPayInfo(productId, reservationId)
        mainViewModel.setRetryAction { viewModel.reloadData(productId, reservationId) }
    }

    LaunchedEffect(Unit) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.sideEffect.collect {
                when (it) {
                    PaySideEffect.ToastPayFailed -> {
                        Toast.makeText(context, context.getString(R.string.toast_pay_result_failed), Toast.LENGTH_SHORT).show()
                    }
                    PaySideEffect.ToastPaidMessageSendSuccess -> {
                        Toast.makeText(context, context.getString(R.string.toast_pay_result_paid_message_send_success), Toast.LENGTH_SHORT).show()
                    }
                    PaySideEffect.NavigateBack -> {
                        navHostController.popBackStack()
                    }
                    is PaySideEffect.CommonError -> {
                        mainViewModel.handleError(it.throwable)
                    }
                }
            }
        }
    }

    PayScreen(
        rentalSummary = uiState.rentalSummary,
        basicRentalFee = uiState.basicRentalFee,
        depositAmount = uiState.depositAmount,
        showPayResultDialog = uiState.showPayResultDialog,
        scrollState = scrollState,
        isLoading = uiState.isLoading,
        onBackClick = navHostController::popBackStack,
        onPayClick = { viewModel.updateStatusToPaid(productId, reservationId) },
        onPayResultDismiss = viewModel::showPayResultDialog,
        onPayResultConfirm = viewModel::onConfirmAndNavigateBack
    )
}