package com.example.rentit.presentation.productdetail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.example.rentit.navigation.productdetail.navigateToResvRequest
import com.example.rentit.navigation.productdetail.navigateToResvRequestHistory

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailRoute(navHostController: NavHostController, productId: Int) {
    val viewModel: ProductDetailViewModel = hiltViewModel()
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(Unit) {
        viewModel.setLoading(true)
        viewModel.getProductDetail(productId)
        viewModel.setLoading(false)
    }

    LaunchedEffect(Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.sideEffect.collect {
                when (it) {
                    ProductDetailSideEffect.NavigateToRentalHistory -> {
                        navHostController.navigateToResvRequestHistory(productId)
                    }

                    ProductDetailSideEffect.NavigateToChatting -> {
                        /* TODO */
                    }

                    ProductDetailSideEffect.NavigateToResvRequest -> {
                        navHostController.navigateToResvRequest(productId)
                    }
                }
            }
        }
    }

    ProductDetailScreen(
        productDetail = uiState.productDetail,
        requestCount = uiState.requestCount,
        showFullImage = uiState.showFullImage,
        showBottomSheet = uiState.showBottomSheet,
        sheetState = sheetState,
        onRentalHistoryClick = viewModel::onRentalHistoryClicked,
        onChattingClick = viewModel::onChattingClicked,
        onResvRequestClick = viewModel::onResvRequestClicked,
        onBackClick = navHostController::popBackStack,
        onFullImageDismiss = viewModel::hideFullImage,
        onFullImageShow = viewModel::showFullImage,
        onBottomSheetShow = viewModel::showBottomSheet
    )
}