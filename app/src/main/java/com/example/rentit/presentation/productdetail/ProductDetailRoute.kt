package com.example.rentit.presentation.productdetail

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import com.example.rentit.navigation.chatroom.navigateToChatRoom
import com.example.rentit.navigation.productdetail.navigateToReservation
import com.example.rentit.navigation.productdetail.navigateToRentalHistory
import com.example.rentit.presentation.main.MainViewModel
import com.example.rentit.presentation.productdetail.dialog.ChatUnavailableDialog

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailRoute(navHostController: NavHostController, productId: Int) {
    val backStackEntry = navHostController.currentBackStackEntry
    val mainViewModel: MainViewModel? = backStackEntry?.let { hiltViewModel(it) }

    val viewModel: ProductDetailViewModel = hiltViewModel()
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val menuDrawerState = rememberModalBottomSheetState()
    val imagePagerState = rememberPagerState { uiState.productDetail.imgUrlList.size }
    val fullImagePagerState = rememberPagerState { uiState.productDetail.imgUrlList.size }

    LaunchedEffect(Unit) {
        viewModel.loadProductDetail(productId)
        mainViewModel?.setRetryAction { viewModel.retryLoadProductDetail(productId) }
    }

    LaunchedEffect(Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.sideEffect.collect {
                when (it) {
                    ProductDetailSideEffect.NavigateToRentalHistory -> {
                        navHostController.navigateToRentalHistory(productId)
                    }
                    is ProductDetailSideEffect.NavigateToChatting -> {
                        navHostController.navigateToChatRoom(it.chatRoomId)
                    }
                    ProductDetailSideEffect.NavigateToResvRequest -> {
                        navHostController.navigateToReservation(productId)
                    }
                    ProductDetailSideEffect.ToastComingSoon -> {
                        Toast.makeText(context, R.string.common_toast_feat_coming_soon, Toast.LENGTH_SHORT).show()
                    }
                    is ProductDetailSideEffect.CommonError -> {
                        mainViewModel?.handleError(it.throwable)
                    }
                }
            }
        }
    }

    LaunchedEffect(uiState.showFullImage) {
        if(uiState.showFullImage) {
            fullImagePagerState.scrollToPage(imagePagerState.currentPage)
        }
    }

    ProductDetailScreen(
        productDetail = uiState.productDetail,
        reservedDateList = uiState.reservedDateList,
        requestCount = uiState.requestCount,
        menuDrawerState = menuDrawerState,
        bottomSheetState = bottomSheetState,
        imagePagerState = imagePagerState,
        fullImagePagerState = fullImagePagerState,
        isUserOwner = uiState.isUserOwner,
        showFullImage = uiState.showFullImage,
        showMenuDrawer = uiState.showMenuDrawer,
        showBottomSheet = uiState.showBottomSheet,
        onRentalHistoryClick = viewModel::onRentalHistoryClicked,
        onChattingClick = { viewModel.onChattingClicked(productId) },
        onResvRequestClick = viewModel::onResvRequestClicked,
        onBackClick = navHostController::popBackStack,
        onEditClick = viewModel::emitComingSoonToast,
        onDeleteClick = viewModel::emitComingSoonToast,
        onMenuDrawerShow = viewModel::showMenuDrawer,
        onMenuDrawerDismiss = viewModel::hideMenuDrawer,
        onFullImageDismiss = viewModel::hideFullImage,
        onFullImageShow = viewModel::showFullImage,
        onBottomSheetShow = viewModel::showBottomSheet,
        onBottomSheetDismiss = viewModel::hideBottomSheet,
        onLikeClick = viewModel::emitComingSoonToast,
        onShareClick = viewModel::emitComingSoonToast,
    )

    if(uiState.showChatUnavailableDialog) {
        ChatUnavailableDialog(viewModel::hideChatUnavailableDialog)
    }
}