package com.example.rentit.presentation.productdetail.rentalhistory

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RentalHistoryRoute(navHostController: NavHostController, productId: Int) {
    val viewModel: RentalHistoryViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val historyListScrollState = remember { LazyListState() }

    var calendarMonth by remember { mutableStateOf(YearMonth.now()) }

    LaunchedEffect(productId) {
        viewModel.getProductRequestList(productId)
    }

    LaunchedEffect(uiState.filterMode) {
        historyListScrollState.animateScrollToItem(0)
    }

    RentalHistoryScreen(
        rentalHistoryList = uiState.filteredRentalHistoryList,
        filterMode = uiState.filterMode,
        calendarMonth = calendarMonth,
        historyListScrollState = historyListScrollState,
        onChangeMonth = { calendarMonth = calendarMonth.plusMonths(it) },
        onBackClick = navHostController::popBackStack,
        onToggleFilter = { viewModel.updateFilterMode(it) }
    )
}
