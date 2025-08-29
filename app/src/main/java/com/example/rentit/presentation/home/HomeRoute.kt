package com.example.rentit.presentation.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.rentit.navigation.productdetail.navigateToProductDetail

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeRoute(navHostController: NavHostController) {
    val viewModel: HomeViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    val scrollState = rememberLazyListState()

    LaunchedEffect(uiState.filterParentCategoryId) {
        scrollState.scrollToItem(0)
    }

    HomeScreen(
        scrollState = scrollState,
        sortedProducts = uiState.filteredProductList,
        parentIdToNameCategoryMap = uiState.parentIdToNameCategoryMap,
        filterParentCategoryId = uiState.filterParentCategoryId,
        onlyRentalAvailable = uiState.onlyRentalAvailable,
        isLoading = uiState.isLoading,
        showNetworkErrorDialog = uiState.showNetworkErrorDialog,
        showServerErrorDialog = uiState.showServerErrorDialog,
        onToggleRentalAvailableFilter = viewModel::toggleOnlyRentalAvailable,
        onSelectCategory = viewModel::filterByParentCategory,
        onProductClick = navHostController::navigateToProductDetail,
        onRetry = viewModel::retryFetchProductList
    )
}