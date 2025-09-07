package com.example.rentit.presentation.mypage

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
import com.example.rentit.navigation.productdetail.navigateToProductDetail
import com.example.rentit.navigation.rentaldetail.navigateToRentalDetail
import com.example.rentit.navigation.setting.navigateToSetting

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyPageRoute(navHostController: NavHostController) {
    val viewModel: MyPageViewModel = hiltViewModel()
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getMyProductList()
        viewModel.getMyRentalList()
    }

    LaunchedEffect(Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.sideEffect.collect {
                when(it) {
                    is MyPageSideEffect.NavigateToProductDetail -> {
                        navHostController.navigateToProductDetail(it.productId)
                    }
                    is MyPageSideEffect.NavigateToRentalDetail -> {
                        navHostController.navigateToRentalDetail(it.productId, it.reservationId)
                    }
                    is MyPageSideEffect.NavigateToSetting -> {
                        navHostController.navigateToSetting()
                    }
                }
            }
        }
    }

    MyPageScreen(
        isFirstTabSelected = uiState.isFirstTabSelected,
        myProductList = uiState.myProductList,
        myRentalList = uiState.myRentalList,
        onTabActive = viewModel::setTabSelected,
        onProductItemClick = viewModel::onProductItemClicked,
        onRentalItemClick = viewModel::onRentalItemClicked,
        onSettingClick = viewModel::onSettingClicked
    )
}