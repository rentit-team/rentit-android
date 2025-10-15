package com.example.rentit.navigation.myproductsrental

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.rentit.presentation.mypage.myproductsrental.MyProductsRentalRoute

fun NavHostController.navigateToMyProductsRental() {
    navigate(
        route = MyProductsRentalRoute.MyProductsRental
    )
}

fun NavGraphBuilder.myProductsRentalGraph(navHostController: NavHostController) {
    composable<MyProductsRentalRoute.MyProductsRental> {
        MyProductsRentalRoute(navHostController)
    }
}