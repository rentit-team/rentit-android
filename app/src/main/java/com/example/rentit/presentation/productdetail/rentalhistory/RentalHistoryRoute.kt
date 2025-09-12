package com.example.rentit.presentation.productdetail.rentalhistory

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.example.rentit.common.component.layout.LoadingScreen
import com.example.rentit.navigation.rentaldetail.navigateToRentalDetail
import com.example.rentit.presentation.main.MainViewModel
import com.example.rentit.presentation.productdetail.rentalhistory.dialog.AccessForbiddenDialog

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RentalHistoryRoute(navHostController: NavHostController, productId: Int) {
    val backStackEntry = navHostController.currentBackStackEntry
    val mainViewModel: MainViewModel? = backStackEntry?.let { hiltViewModel(it) }

    val viewModel: RentalHistoryViewModel = hiltViewModel()
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(productId) {
        viewModel.loadProductRentalHistories(productId)
        mainViewModel?.setRetryAction { viewModel.retryLoadHistories(productId) }
    }

    LaunchedEffect(Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.sideEffect.collect {
                when (it) {
                    is RentalHistorySideEffect.NavigateToRentalDetail -> {
                        navHostController.navigateToRentalDetail(productId, it.reservationId)
                    }
                    is RentalHistorySideEffect.CommonError -> {
                        mainViewModel?.handleError(it.throwable)
                    }
                }
            }
        }
    }

    LaunchedEffect(uiState.filterMode, uiState.selectedReservationId) {
        viewModel.scrollToTop()
    }

    RentalHistoryScreen(
        rentalHistoryList = uiState.filteredRentalHistoryList,
        rentalHistoryByDateMap = uiState.rentalHistoryByDateMap,
        filterMode = uiState.filterMode,
        calendarMonth = uiState.calendarMonth,
        historyListScrollState = uiState.historyListScrollState,
        onChangeMonth = viewModel::updateCalendarMonth,
        onRentalDateClick = viewModel::updateSelectedRentalDate,
        onHistoryClick = viewModel::onHistoryClicked,
        onToggleFilter = viewModel::updateFilterMode,
        onBackClick = navHostController::popBackStack,
    )

    LoadingScreen(uiState.isLoading)

    if(uiState.showAccessForbiddenDialog) {
        AccessForbiddenDialog(navigateBack = navHostController::popBackStack)
    }
}
