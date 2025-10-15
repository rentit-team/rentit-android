package com.example.rentit.presentation.mypage

import android.widget.Toast
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.example.rentit.R
import com.example.rentit.common.ui.component.layout.RentItLoadingScreen
import com.example.rentit.navigation.myproductsrental.navigateToMyProductsRental
import com.example.rentit.navigation.productdetail.navigateToProductDetail
import com.example.rentit.navigation.rentaldetail.navigateToRentalDetail
import com.example.rentit.navigation.setting.navigateToSetting
import com.example.rentit.presentation.main.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPageRoute(navHostController: NavHostController) {
    val mainViewModel: MainViewModel = hiltViewModel()

    val viewModel: MyPageViewModel = hiltViewModel()
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val pullToRefreshState = rememberPullToRefreshState()

    LaunchedEffect(Unit) {
        viewModel.loadInitialData()
        mainViewModel.setRetryAction(viewModel::reloadData)
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
                    is MyPageSideEffect.NavigateToMyProductsRental -> {
                        navHostController.navigateToMyProductsRental()
                    }
                    MyPageSideEffect.NavigateToSetting -> {
                        navHostController.navigateToSetting()
                    }
                    MyPageSideEffect.ToastComingSoon -> {
                        Toast.makeText(context, context.getString(R.string.common_toast_feat_coming_soon), Toast.LENGTH_SHORT).show()
                    }
                    is MyPageSideEffect.CommonError -> {
                        mainViewModel.handleError(it.throwable)
                    }
                }
            }
        }
    }

    MyPageScreen(
        profileImgUrl = uiState.profileImgUrl,
        nickName = uiState.nickName,
        myProductCount = uiState.myProductCount,
        myRentingCount = uiState.myValidRentalCount,
        myPendingRentalCount = uiState.myPendingRentalCount,
        nearestDueItem = uiState.nearestDueItem,
        myProductList = uiState.myProductList,
        myRentalList = uiState.myRentalList,
        pullToRefreshState = pullToRefreshState,
        currentTab = uiState.currentTab,
        isRefreshing = uiState.isRefreshing,
        onRefresh = viewModel::refreshData,
        onAlertClick = viewModel::showComingSoonMessage,
        onInfoRentalDetailClick = viewModel::onInfoRentalDetailClicked,
        onTabActive = viewModel::setTabSelected,
        onProductItemClick = viewModel::onProductItemClicked,
        onRentalItemClick = viewModel::onRentalItemClicked,
        onSettingClick = viewModel::onSettingClicked,
        onMyProductCountClick = viewModel::onMyProductCountClicked,
        onMyRentingCountClick = viewModel::onMyRentingCountClicked,
        onMyPendingRentalCountClick = viewModel::onMyPendingRentalClicked,
    )

    RentItLoadingScreen(uiState.isLoading)
}