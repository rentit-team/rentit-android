package com.example.rentit.presentation.mypage.myproductsrental

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
fun MyProductsRentalRoute(navHostController: NavHostController) {
    val viewModel: MyProductsRentalViewModel = hiltViewModel()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getMyProductsRentalHistories()
    }

    MyProductsRentalScreen(
        rentals = uiState.filteredRentalHistories,
        selectedFilter = uiState.selectedFilter,
        historyCountMap = uiState.historyCountMap,
        upcomingShipmentCount = uiState.upcomingShipmentCount,
        showNoticeBanner = uiState.showNoticeBanner,
        onFilterChange = viewModel::onFilterChanged,
        onBackClick = navHostController::popBackStack,
    )
}