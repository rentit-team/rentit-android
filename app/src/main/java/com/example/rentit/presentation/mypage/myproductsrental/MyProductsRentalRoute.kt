package com.example.rentit.presentation.mypage.myproductsrental

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
import com.example.rentit.navigation.productdetail.navigateToRentalHistory
import com.example.rentit.presentation.main.MainViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyProductsRentalRoute(navHostController: NavHostController) {
    val backStackEntry = navHostController.currentBackStackEntry
    val mainViewModel: MainViewModel? = backStackEntry?.let { hiltViewModel(it) }

    val viewModel: MyProductsRentalViewModel = hiltViewModel()
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getMyProductsRentalHistories()
        mainViewModel?.setRetryAction(viewModel::reloadData)
    }

    LaunchedEffect(Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.sideEffect.collect {
                when(it) {
                    is MyProductsRentalSideEffect.CommonError -> {
                        mainViewModel?.handleError(it.throwable)
                    }
                    is MyProductsRentalSideEffect.NavigateToProductDetail -> {
                        navHostController.navigateToRentalHistory(it.productId, it.selectedReservationId, it.rentalStatus)
                    }
                }
            }
        }
    }

    MyProductsRentalScreen(
        rentals = uiState.filteredRentalHistories,
        selectedFilter = uiState.selectedFilter,
        historyCountMap = uiState.historyCountMap,
        upcomingShipmentCount = uiState.upcomingShipmentCount,
        showNoticeBanner = uiState.showNoticeBanner,
        onFilterChange = viewModel::onFilterChanged,
        onItemClick = viewModel::onItemClicked,
        onBackClick = navHostController::popBackStack,
    )

    LoadingScreen(uiState.isLoading)
}