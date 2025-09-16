package com.example.rentit.navigation.productdetail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.rentit.common.enums.RentalStatus
import com.example.rentit.navigation.bottomtab.BottomTabRoute
import com.example.rentit.presentation.productdetail.ProductDetailRoute
import com.example.rentit.presentation.productdetail.rentalhistory.RentalHistoryRoute
import com.example.rentit.presentation.productdetail.reservation.ReservationRoute
import com.example.rentit.presentation.productdetail.reservation.complete.ReservationCompleteRoute

fun NavHostController.navigateToProductDetail(productId: Int) {
    navigate(
        route = ProductDetailRoute.ProductDetail(productId)
    )
}

fun NavHostController.navigateToProductDetailFromCreate(productId: Int) {
    navigate(
        route = ProductDetailRoute.ProductDetail(productId)
    ) {
        popUpTo(BottomTabRoute.Home.route){
            inclusive = false
        }
    }
}

fun NavHostController.navigateToReservation(productId: Int) {
    navigate(
        route = ProductDetailRoute.Reservation(productId)
    )
}

fun NavHostController.navigateToReservationComplete(
    productId: Int = 0,
    reservationId: Int = 0,
    rentalStartDate: String = "",
    rentalEndDate: String = "",
    totalPrice: Int = 0
) {
    navigate(
        route = ProductDetailRoute.ReservationComplete(
            productId,
            reservationId,
            rentalStartDate,
            rentalEndDate,
            totalPrice
        )
    )
}

fun NavHostController.navigateToRentalHistory(productId: Int, selectedReservationId: Int? = null, initialRentalStatus: RentalStatus? = null) {
    navigate(
        route = ProductDetailRoute.RentalHistory(productId, selectedReservationId, initialRentalStatus)
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.productDetailGraph(navHostController: NavHostController) {
    composable<ProductDetailRoute.ProductDetail> { backStackEntry ->
        val items: ProductDetailRoute.ProductDetail = backStackEntry.toRoute()
        ProductDetailRoute(navHostController, items.productId)
    }
    composable<ProductDetailRoute.Reservation> { backStackEntry ->
        val items: ProductDetailRoute.Reservation = backStackEntry.toRoute()
        ReservationRoute(navHostController, items.productId)
    }
    composable<ProductDetailRoute.ReservationComplete> { backStackEntry ->
        val items: ProductDetailRoute.ReservationComplete = backStackEntry.toRoute()
        ReservationCompleteRoute(
            navHostController,
            items.productId,
            items.reservationId,
            items.rentalStartDate,
            items.rentalEndDate,
            items.totalPrice
        )
    }
    composable<ProductDetailRoute.RentalHistory> { backStackEntry ->
        val items: ProductDetailRoute.RentalHistory = backStackEntry.toRoute()
        RentalHistoryRoute(navHostController, items.productId, items.selectedReservationId, items.initialRentalStatus)
    }
}
