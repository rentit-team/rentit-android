package com.example.rentit.navigation.pay

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.rentit.presentation.pay.PayRoute
import com.example.rentit.presentation.pay.PayUiModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun NavHostController.navigateToPay(productId: Int, reservationId: Int, payInfo: PayUiModel) {
    val payInfoJson = Json.encodeToString(payInfo)
    navigate(
        route = PayRoute.Pay(productId, reservationId, payInfoJson)
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.payGraph(navHostController: NavHostController) {
    composable<PayRoute.Pay> { backStackEntry ->
        val items: PayRoute.Pay = backStackEntry.toRoute()
        val payInfo = Json.decodeFromString<PayUiModel>(items.payInfoJson)
        PayRoute(navHostController, items.productId, items.reservationId, payInfo)
    }
}