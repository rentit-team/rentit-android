package com.example.rentit.domain.user.model

import com.example.rentit.common.enums.RentalStatus

data class MyRentalItemModel(
    val productId: Int,
    val reservationId: Int,
    val productTitle: String,
    val thumbnailImgUrl: String?,
    val startDate: String, // "YYYY-MM-DD"
    val endDate: String, // "YYYY-MM-DD"
    val status: RentalStatus,
    val requestedAt: String // ISO 8601
)