package com.example.rentit.domain.rental.model

import com.example.rentit.common.enums.RentalStatus
import com.example.rentit.common.uimodel.RentalPeriodModel

data class RentalHistoryModel(
    val reservationId: Int,
    val productId: Int,
    val status: RentalStatus,
    val renterNickName: String,
    val rentalPeriod: RentalPeriodModel,
    val requestedAt: String,
)