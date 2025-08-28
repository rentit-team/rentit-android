package com.example.rentit.presentation.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.rentit.navigation.productdetail.navigateToProductDetail

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeRoute(navHostController: NavHostController) {
    val viewModel: HomeViewModel = hiltViewModel()
    val productList by viewModel.productList.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    HomeScreen(
        sortedProducts = productList,
        isLoading = uiState.isLoading,
        showNetworkErrorDialog = uiState.showNetworkErrorDialog,
        showServerErrorDialog = uiState.showServerErrorDialog,
        onProductClick = navHostController::navigateToProductDetail,
        onRetry = viewModel::retryFetchProductList
    )
}