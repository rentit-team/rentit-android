package com.example.rentit.presentation.productdetail.rentalhistory

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RentalHistoryRoute(navHostController: NavHostController, productId: Int) {
    val viewModel: RentalHistoryViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(productId) {
        viewModel.getProductRequestList(productId)
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
        onToggleFilter = viewModel::updateFilterMode,
        onBackClick = navHostController::popBackStack,
    )
}
