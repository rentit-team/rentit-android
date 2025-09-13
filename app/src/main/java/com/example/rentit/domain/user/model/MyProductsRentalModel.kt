package com.example.rentit.domain.user.model

data class MyProductsRentalModel(
    val productId: Int,
    val reservationId: Int,
    val productThumbnailUrl: String?,
    val rentalCount: Int = 0,
    val productTitle: String,
    val renterNickname: String = "",
    val totalExpectRevenue: Int = 0,
    val daysBeforeStart: Int = 0,
    val daysBeforeReturn: Int = 0
)