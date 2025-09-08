package com.example.rentit.domain.user.model

data class NearestDueItemModel(
    val reservationId: Int,
    val productId: Int,
    val productTitle: String,
    val remainingRentalDays: Int
)