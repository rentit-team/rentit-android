package com.example.rentit.navigation.productdetail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.rentit.presentation.productdetail.ProductDetailScreen
import com.example.rentit.presentation.productdetail.reservation.request.ResvRequestScreen
import com.example.rentit.presentation.productdetail.reservation.request.complete.ResvRequestCompleteScreen
import com.example.rentit.presentation.productdetail.reservation.requesthistory.RequestHistoryScreen

fun NavHostController.navigateToProductDetail(productId: Int?) {
    navigate(
        route = ProductDetailRoute.ProductDetail(productId)
    )
}

fun NavHostController.navigateToResvRequest(productId: Int?) {
    navigate(
        route = ProductDetailRoute.ResvRequest(productId)
    )
}

fun NavHostController.navigateToResvRequestComplete(
    rentalStartDate: String = "",
    rentalEndDate: String = "",
    formattedTotalPrice: String = "0",
) {
    navigate(
        route = ProductDetailRoute.ResvRequestComplete(
            rentalStartDate,
            rentalEndDate,
            formattedTotalPrice
        )
    )
}

fun NavHostController.navigateToResvRequestHistory(productId: Int?) {
    navigate(
        route = ProductDetailRoute.ResvRequestHistory(productId)
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.productDetailGraph(navHostController: NavHostController) {
    composable<ProductDetailRoute.ProductDetail> { backStackEntry ->
        val items: ProductDetailRoute.ProductDetail = backStackEntry.toRoute()
        ProductDetailScreen(navHostController, items.productId)
    }
    composable<ProductDetailRoute.ResvRequest> { backStackEntry ->
        val items: ProductDetailRoute.ResvRequest = backStackEntry.toRoute()
        ResvRequestScreen(navHostController, items.productId)
    }
    composable<ProductDetailRoute.ResvRequestComplete> { backStackEntry ->
        val items: ProductDetailRoute.ResvRequestComplete = backStackEntry.toRoute()
        ResvRequestCompleteScreen(
            navHostController,
            items.rentalStartDate,
            items.rentalEndDate,
            items.formattedTotalPrice
        )
    }
    composable<ProductDetailRoute.ResvRequestHistory> { backStackEntry ->
        val items: ProductDetailRoute.ResvRequestHistory = backStackEntry.toRoute()
        RequestHistoryScreen(navHostController, items.productId)
    }
}
