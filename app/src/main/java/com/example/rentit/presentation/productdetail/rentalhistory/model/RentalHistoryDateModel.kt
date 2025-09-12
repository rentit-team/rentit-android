package com.example.rentit.presentation.productdetail.rentalhistory.model

import com.example.rentit.common.enums.RentalStatus

data class RentalHistoryDateModel(
    val reservationId: Int,
    val rentalStatus: RentalStatus,
)