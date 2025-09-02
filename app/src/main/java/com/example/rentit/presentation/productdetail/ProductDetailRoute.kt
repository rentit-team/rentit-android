package com.example.rentit.presentation.productdetail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.rentit.domain.product.model.ProductDetailModel
import com.example.rentit.navigation.productdetail.navigateToResvRequest
import com.example.rentit.navigation.productdetail.navigateToResvRequestHistory

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailRoute(navHostController: NavHostController, productId: Int) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by remember { mutableStateOf(false) }
    var showFullImage by remember { mutableStateOf(false) }

    val productDetailViewModel: ProductDetailViewModel = hiltViewModel()
    val productDetailResult by productDetailViewModel.productDetail.collectAsStateWithLifecycle()
    val requestHistory by productDetailViewModel.requestList.collectAsStateWithLifecycle()

    val productDetail = productDetailResult?.getOrNull()?.product
    val ownerId = productDetail?.owner?.userId ?: -1

    val authUserId = productDetailViewModel.getAuthUserIdFromPrefs()
    val isMyProduct = ownerId > -1 && authUserId.toInt() == ownerId

    LaunchedEffect(productId) {
        productDetailViewModel.getProductDetail(productId)
        if (isMyProduct)
            productDetailViewModel.getProductRequestList(productId)
    }

    val sampleProductDetail = ProductDetailModel(
        productId = 101,
        price = 50000,
        title = "Mountain Bike",
        category = "Sports",
        content = "A sturdy mountain bike suitable for off-road trails.",
        createdAt = "2025-09-01T10:00:00",
        imgUrlList = listOf(
            "https://image.utoimage.com/preview/cp872722/2022/12/202212008462_500.jpg",
            "https://gongu.copyright.or.kr/gongu/wrt/cmmn/wrtFileImageView.do?wrtSn=9046601&filePath=L2Rpc2sxL25ld2RhdGEvMjAxNC8yMS9DTFM2L2FzYWRhbFBob3RvXzI0MTRfMjAxNDA0MTY=&thumbAt=Y&thumbSe=b_tbumb&wrtTy=10004",
            "https://discuss.pytorch.kr/uploads/default/original/2X/e/e9fd2722c675bd14bc1a19c777a3352a3d42d261.jpeg"
        ),
    )

    ProductDetailScreen(
        productDetail = sampleProductDetail,
        isMyProduct = showFullImage,
        requestCount = requestHistory.size,
        showFullImage = showFullImage,
        showBottomSheet = showBottomSheet,
        sheetState = sheetState,
        navigateToRentalHistory = { navHostController.navigateToResvRequestHistory(productId) },
        navigateToChatting = { /* TODO */ },
        navigateToResvRequest = { navHostController.navigateToResvRequest(productId) },
        navigateBack = { navHostController.popBackStack() },
        onFullImageDismiss = { showFullImage = false },
        onFullImageShow = { showFullImage = true },
        onBottomSheetShow = { showBottomSheet = false }
    )
}