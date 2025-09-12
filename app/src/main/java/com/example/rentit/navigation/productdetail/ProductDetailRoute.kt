package com.example.rentit.navigation.productdetail

import kotlinx.serialization.Serializable

sealed class ProductDetailRoute {
    @Serializable
    data class ProductDetail(val productId: Int) : ProductDetailRoute()

    @Serializable
    data class Reservation(val productId: Int) : ProductDetailRoute()

    @Serializable
    data class ReservationComplete(
        val rentalStartDate: String,
        val rentalEndDate: String,
        val formattedTotalPrice: String,
    ) : ProductDetailRoute()

    @Serializable
    data class RentalHistory(val productId: Int) : ProductDetailRoute()
}