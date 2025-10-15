package com.example.rentit.presentation.home

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeRoute(navHostController: NavHostController) {
    val mainViewModel: MainViewModel = hiltViewModel()

    val viewModel: HomeViewModel = hiltViewModel()
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by viewModel.uiState.collectAsState()

    val pullRefreshState = rememberPullToRefreshState()

    LaunchedEffect(Unit) {
        viewModel.fetchHomeData()
        mainViewModel.setRetryAction(viewModel::retryFetchHomeData)
    }

    LaunchedEffect(Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.sideEffect.collect {
                when(it) {
                    HomeSideEffect.ScrollToTop -> {
                        uiState.scrollState.animateScrollToItem(0)
                    }
                    is HomeSideEffect.CommonError -> {
                        println("error: ${it.throwable.message}")
                        mainViewModel.handleError(it.throwable)
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
        pullToRefreshState = pullRefreshState,
        isLoading = uiState.isLoading,
        isRefreshing = uiState.isRefreshing,
        onRefresh = viewModel::refreshHomeData,
        onToggleRentalAvailableFilter = viewModel::toggleOnlyRentalAvailable,
        onSelectCategory = viewModel::filterByParentCategory,
        onProductClick = navHostController::navigateToProductDetail,
    )
}