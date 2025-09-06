package com.example.rentit.navigation.rentaldetail

import kotlinx.serialization.Serializable

sealed class RentalDetailRoute {
    @Serializable
    data class RentalDetail(val productId: Int, val reservationId: Int) : RentalDetailRoute()

    @Serializable
    data class RentalPhotoCheck(val productId: Int, val reservationId: Int) : RentalDetailRoute()

    @Serializable
    data class PhotoBeforeRent(val productId: Int, val reservationId: Int) : RentalDetailRoute()

    @Serializable
    data class PhotoBeforeReturn(val productId: Int, val reservationId: Int) : RentalDetailRoute()
}