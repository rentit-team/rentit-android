package com.example.rentit.presentation.productdetail.reservation.complete

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.rentit.navigation.rentaldetail.navigateToRentalDetailPopUpToHome

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReservationCompleteRoute(
    navHostController: NavHostController,
    productId: Int,
    reservationId: Int,
    rentalStartDate: String,
    rentalEndDate: String,
    totalPrice: Int
) {
    ReservationCompleteScreen(
        rentalStartDate = rentalStartDate,
        rentalEndDate = rentalEndDate,
        totalPrice = totalPrice,
        onConfirmClick = { navHostController.navigateToRentalDetailPopUpToHome(productId, reservationId) }
    )
}