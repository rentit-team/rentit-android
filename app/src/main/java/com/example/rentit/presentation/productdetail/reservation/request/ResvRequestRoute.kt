package com.example.rentit.presentation.productdetail.reservation.request

import android.os.Build
import androidx.annotation.RequiresApi
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
import com.example.rentit.navigation.productdetail.navigateToResvRequestComplete

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ResvRequestRoute(navHostController: NavHostController, productId: Int) {
    val viewModel: ResvRequestViewModel = hiltViewModel()
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getProductDetail(productId)
        viewModel.getReservedDates(productId)
    }

    LaunchedEffect(Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.sideEffect.collect { sideEffect -> }
        }
    }

    navHostController.navigateToResvRequestComplete(
        rentalStartDate = uiState.rentalStartDate.toString(),
        rentalEndDate = uiState.rentalEndDate.toString(),
        formattedTotalPrice = ""
    )

    ResvRequestScreen(
        rentalStartDate = uiState.rentalStartDate,
        rentalEndDate = uiState.rentalEndDate,
        rentalPeriod = uiState.rentalPeriod,
        reservedDateList = uiState.reservedDateList,
        rentalPrice = uiState.rentalPrice,
        totalPrice = uiState.totalPrice,
        deposit = uiState.deposit,
        onBackClick = navHostController::popBackStack,
        onResvRequestClick = { viewModel.postResv(productId) },
        onSetRentalStartDate = viewModel::setRentalStartDate,
        onSetRentalEndDate = viewModel::setRentalEndDate
    ) 
}