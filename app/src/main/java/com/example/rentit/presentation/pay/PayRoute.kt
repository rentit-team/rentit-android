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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PayRoute(navHostController: NavHostController, productId: Int, reservationId: Int) {

    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val viewModel: PayViewModel = hiltViewModel()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.getPayInfo(productId, reservationId)
    }

    LaunchedEffect(Unit) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.sideEffect.collect {
                when (it) {
                    PaySideEffect.ToastPayFailed -> {
                        Toast.makeText(context, context.getString(R.string.toast_pay_result_failed), Toast.LENGTH_SHORT).show()
                    }
                    PaySideEffect.NavigateBackToRentalDetail -> {
                        navHostController.popBackStack()
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
        showLoadFailedDialog = uiState.showLoadErrorDialog,
        scrollState = scrollState,
        isLoading = uiState.isLoading,
        onBackClick = viewModel::navigateBackToRentalDetail,
        onPayClick = { viewModel.updateStatusToPaid(productId, reservationId) },
        onLoadErrorDismiss = viewModel::dismissLoadErrorDialogAndNavigateBack,
        onPayResultDismiss = viewModel::showPayResultDialog,
        onPayResultConfirm = viewModel::onConfirmAndNavigateBack
    )
}