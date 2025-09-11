package com.example.rentit.domain.rental.model

import com.example.rentit.common.enums.RentalStatus
import com.example.rentit.common.uimodel.RentalPeriodModel

data class RentalHistoryListItemModel(
    val reservationId: Int,
    val productId: Int,
    val status: RentalStatus,
    val nickname: String,
    val rentalPeriod: RentalPeriodModel,
    val createdAt: String,
)