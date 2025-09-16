package com.example.rentit.navigation.productdetail

import com.example.rentit.common.enums.RentalStatus
import kotlinx.serialization.Serializable

sealed class ProductDetailRoute {
    @Serializable
    data class ProductDetail(val productId: Int) : ProductDetailRoute()

    @Serializable
    data class Reservation(val productId: Int) : ProductDetailRoute()

    @Serializable
    data class ReservationComplete(
        val productId: Int,
        val reservationId: Int,
        val rentalStartDate: String,
        val rentalEndDate: String,
        val totalPrice: Int,
    ) : ProductDetailRoute()

    @Serializable
    data class RentalHistory(val productId: Int, val selectedReservationId: Int?, val initialRentalStatus: RentalStatus?) : ProductDetailRoute()
}