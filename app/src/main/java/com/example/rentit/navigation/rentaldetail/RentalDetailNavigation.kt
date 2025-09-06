package com.example.rentit.navigation.rentaldetail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.rentit.presentation.rentaldetail.RentalDetailRoute
import com.example.rentit.presentation.rentaldetail.photobeforerent.PhotoBeforeRentRoute
import com.example.rentit.presentation.rentaldetail.rentalphotocheck.RentalPhotoCheckRoute
import com.example.rentit.presentation.rentaldetail.photobeforereturn.PhotoBeforeReturnRoute

fun NavHostController.navigateToRentalDetail(productId: Int, reservationId: Int) {
    navigate(
        route = RentalDetailRoute.RentalDetail(productId, reservationId)
    )
}

fun NavHostController.navigateToRentalPhotoCheck(productId: Int, reservationId: Int) {
    navigate(
        route = RentalDetailRoute.RentalPhotoCheck(productId, reservationId)
    )
}

fun NavHostController.navigateToPhotoBeforeRent(productId: Int, reservationId: Int) {
    navigate(
        route = RentalDetailRoute.PhotoBeforeRent(productId, reservationId)
    )
}

fun NavHostController.navigateToPhotoBeforeReturn(productId: Int, reservationId: Int) {
    navigate(
        route = RentalDetailRoute.PhotoBeforeReturn(productId, reservationId)
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.rentalDetailGraph(navHostController: NavHostController) {
    composable<RentalDetailRoute.RentalDetail> { backStackEntry ->
        val items: RentalDetailRoute.RentalDetail = backStackEntry.toRoute()
        RentalDetailRoute(navHostController, items.productId, items.reservationId)
    }
    composable<RentalDetailRoute.RentalPhotoCheck> { backStackEntry ->
        val items: RentalDetailRoute.RentalPhotoCheck = backStackEntry.toRoute()
        RentalPhotoCheckRoute(navHostController, items.productId, items.reservationId)
    }
    composable<RentalDetailRoute.PhotoBeforeRent> { backStackEntry ->
        val items: RentalDetailRoute.PhotoBeforeRent = backStackEntry.toRoute()
        PhotoBeforeRentRoute(navHostController, items.productId, items.reservationId)
    }

    composable<RentalDetailRoute.PhotoBeforeReturn> { backStackEntry ->
        val items: RentalDetailRoute.PhotoBeforeReturn = backStackEntry.toRoute()
        PhotoBeforeReturnRoute(navHostController, items.productId, items.reservationId)
    }
}
