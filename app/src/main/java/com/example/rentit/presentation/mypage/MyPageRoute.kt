package com.example.rentit.presentation.mypage

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.example.rentit.R
import com.example.rentit.common.component.dialog.NetworkErrorDialog
import com.example.rentit.common.component.dialog.ServerErrorDialog
import com.example.rentit.common.component.layout.LoadingScreen
import com.example.rentit.navigation.productdetail.navigateToProductDetail
import com.example.rentit.navigation.rentaldetail.navigateToRentalDetail
import com.example.rentit.navigation.setting.navigateToSetting

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyPageRoute(navHostController: NavHostController) {
    val viewModel: MyPageViewModel = hiltViewModel()
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadInitialData()
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
                    MyPageSideEffect.NavigateToSetting -> {
                        navHostController.navigateToSetting()
                    }
                    MyPageSideEffect.ToastComingSoon -> {
                        Toast.makeText(context, context.getString(R.string.common_toast_feat_coming_soon), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.dismissAllDialog()
        }
    }

    MyPageScreen(
        profileImgUrl = uiState.profileImgUrl,
        nickName = uiState.nickName,
        infoProductTitle = uiState.infoProductTitle,
        infoRemainingRentalDays = uiState.infoRemainingRentalDays,
        isFirstTabSelected = uiState.isFirstTabSelected,
        myProductList = uiState.myProductList,
        myRentalList = uiState.myRentalList,
        onAlertClick = viewModel::showComingSoonMessage,
        onMyHistoryClick = viewModel::showComingSoonMessage,
        onInfoRentalDetailClick = viewModel::onInfoRentalDetailClicked,
        onTabActive = viewModel::setTabSelected,
        onProductItemClick = viewModel::onProductItemClicked,
        onRentalItemClick = viewModel::onRentalItemClicked,
        onSettingClick = viewModel::onSettingClicked
    )

    LoadingScreen(uiState.isLoading)

    if(uiState.showNetworkErrorDialog) {
        NetworkErrorDialog(
            navigateBack = navHostController::popBackStack,
            onRetry = viewModel::reloadData,
        )
    }

    if(uiState.showServerErrorDialog) {
        ServerErrorDialog(
            navigateBack = navHostController::popBackStack,
            onRetry = viewModel::reloadData
        )
    }
}