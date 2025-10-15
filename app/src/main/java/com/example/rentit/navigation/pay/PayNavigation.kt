package com.example.rentit.navigation.pay

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.rentit.presentation.pay.PayRoute

fun NavHostController.navigateToPay(productId: Int, reservationId: Int) {
    navigate(
        route = PayRoute.Pay(productId, reservationId)
    )
}

fun NavGraphBuilder.payGraph(navHostController: NavHostController) {
    composable<PayRoute.Pay> { backStackEntry ->
        val items: PayRoute.Pay = backStackEntry.toRoute()
        PayRoute(navHostController, items.productId, items.reservationId)
    }
}