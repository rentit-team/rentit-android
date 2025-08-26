package com.example.rentit.navigation.rentaldetail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.rentit.presentation.rentaldetail.owner.OwnerRentalDetailRoute
import com.example.rentit.presentation.rentaldetail.owner.photobeforerent.PhotoBeforeRentRoute
import com.example.rentit.presentation.rentaldetail.rentalphotocheck.RentalPhotoCheckRoute
import com.example.rentit.presentation.rentaldetail.renter.RenterRentalDetailRoute
import com.example.rentit.presentation.rentaldetail.renter.photobeforereturn.PhotoBeforeReturnRoute

fun NavHostController.navigateToOwnerRentalDetail(productId: Int, reservationId: Int) {
    navigate(
        route = RentalDetailRoute.OwnerRentalDetail(productId, reservationId)
    )
}

fun NavHostController.navigateToRenterRentalDetail(productId: Int, reservationId: Int) {
    navigate(
        route = RentalDetailRoute.RenterRentalDetail(productId, reservationId)
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
    composable<RentalDetailRoute.OwnerRentalDetail> { backStackEntry ->
        val items: RentalDetailRoute.OwnerRentalDetail = backStackEntry.toRoute()
        OwnerRentalDetailRoute(navHostController, items.productId, items.reservationId)
    }
    composable<RentalDetailRoute.RenterRentalDetail> { backStackEntry ->
        val items: RentalDetailRoute.RenterRentalDetail = backStackEntry.toRoute()
        RenterRentalDetailRoute(navHostController, items.productId, items.reservationId)
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
