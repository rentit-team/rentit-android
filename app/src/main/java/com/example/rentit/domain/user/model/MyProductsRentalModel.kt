package com.example.rentit.domain.user.model

import com.example.rentit.common.enums.RentalStatus

data class MyProductsRentalModel(
    val productId: Int,
    val reservationId: Int,
    val rentalStatus: RentalStatus,
    val rentalCount: Int,
    val productTitle: String,
    val renterNickname: String,
    val totalExpectRevenue: Int,
    val daysBeforeStart: Int,
    val daysBeforeReturn: Int
)