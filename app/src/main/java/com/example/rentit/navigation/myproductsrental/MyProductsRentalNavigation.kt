package com.example.rentit.navigation.myproductsrental

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.rentit.presentation.mypage.myproductsrental.MyProductsRentalRoute

fun NavHostController.navigateToMyProductsRental() {
    navigate(
        route = MyProductsRentalRoute.MyProductsRental
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.myProductsRentalGraph(navHostController: NavHostController) {
    composable<MyProductsRentalRoute.MyProductsRental> {
        MyProductsRentalRoute(navHostController)
    }
}