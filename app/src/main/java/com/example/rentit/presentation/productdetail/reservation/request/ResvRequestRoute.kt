package com.example.rentit.presentation.productdetail.reservation.request

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.example.rentit.R
import com.example.rentit.common.component.dialog.BaseDialog
import com.example.rentit.common.component.layout.LoadingScreen
import com.example.rentit.common.util.formatPrice
import com.example.rentit.navigation.productdetail.navigateToResvRequestComplete
import com.example.rentit.presentation.main.MainViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ResvRequestRoute(navHostController: NavHostController, productId: Int) {
    val backStackEntry = navHostController.currentBackStackEntry
    val mainViewModel: MainViewModel? = backStackEntry?.let { hiltViewModel(it) }

    val viewModel: ResvRequestViewModel = hiltViewModel()
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val context = LocalContext.current

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadInitialData(productId)
        mainViewModel?.setRetryAction { viewModel.retryDataLoad(productId) }
    }

    LaunchedEffect(Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.sideEffect.collect {
                when (it) {
                    is ResvRequestSideEffect.NavigateToResvRequestComplete -> {
                        navHostController.navigateToResvRequestComplete(
                            rentalStartDate = it.rentalStartDate,
                            rentalEndDate = it.rentalEndDate,
                            formattedTotalPrice = formatPrice(it.totalPrice)
                        )
                    }
                    ResvRequestSideEffect.ToastInvalidPeriod -> {
                        Toast.makeText(context, R.string.toast_invalid_period, Toast.LENGTH_SHORT).show()
                    }
                    ResvRequestSideEffect.ToastPostResvFailed -> {
                        Toast.makeText(context, R.string.toast_post_resv_failed, Toast.LENGTH_SHORT).show()
                    }
                    is ResvRequestSideEffect.CommonError -> {
                        mainViewModel?.handleError(it.throwable)
                    }
                }
            }
        }
    }

    ResvRequestScreen(
        rentalStartDate = uiState.rentalStartDate,
        rentalEndDate = uiState.rentalEndDate,
        selectedPeriod = uiState.selectedPeriod,
        reservedDateList = uiState.reservedDateList,
        minPeriod = uiState.minPeriod,
        maxPeriod = uiState.maxPeriod,
        rentalPrice = uiState.totalRentalPrice,
        totalPrice = uiState.totalPrice,
        deposit = uiState.deposit,
        onBackClick = navHostController::popBackStack,
        onResvRequestClick = { viewModel.postResv(productId) },
        onSetRentalStartDate = viewModel::setRentalStartDate,
        onSetRentalEndDate = viewModel::setRentalEndDate
    )

    LoadingScreen(uiState.isLoading)

    if(uiState.showAccessNotAllowedDialog) {
        BaseDialog(
            title = stringResource(R.string.dialog_owner_access_not_allowed_title),
            content = stringResource(R.string.dialog_owner_access_not_allowed_content),
            confirmBtnText = stringResource(R.string.common_dialog_btn_close),
            onDismissRequest = viewModel::dismissAccessNotAllowedDialog,
            onConfirmRequest = viewModel::dismissAccessNotAllowedDialog
        )
    }

    if(uiState.showResvAlreadyExistDialog) {
        BaseDialog(
            title = stringResource(R.string.dialog_resv_already_exist_title),
            content = stringResource(R.string.dialog_resv_already_exist_content),
            confirmBtnText = stringResource(R.string.common_dialog_btn_close),
            onDismissRequest = viewModel::dismissResvAlreadyExistDialog,
            onConfirmRequest = viewModel::dismissResvAlreadyExistDialog
        )
    }
}