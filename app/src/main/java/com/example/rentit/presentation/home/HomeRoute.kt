package com.example.rentit.presentation.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.example.rentit.navigation.productdetail.navigateToProductDetail
import com.example.rentit.presentation.main.MainViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeRoute(navHostController: NavHostController) {
    val backStackEntry = navHostController.currentBackStackEntry
    val mainViewModel: MainViewModel? = backStackEntry?.let { hiltViewModel(it) }

    val viewModel: HomeViewModel = hiltViewModel()
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchHomeData()
        mainViewModel?.setRetryAction(viewModel::fetchHomeData)
    }

    LaunchedEffect(Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.sideEffect.collect {
                when(it) {
                    HomeSideEffect.ScrollToTop -> {
                        uiState.scrollState.animateScrollToItem(0)
                    }
                    is HomeSideEffect.CommonError -> {
                        mainViewModel?.handleError(it.throwable)
                    }
                }
            }
        }
    }

    HomeScreen(
        scrollState = uiState.scrollState,
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