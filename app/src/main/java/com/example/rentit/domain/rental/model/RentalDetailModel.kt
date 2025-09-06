package com.example.rentit.domain.rental.model

import com.example.rentit.common.enums.RentalRole

data class RentalDetailModel(
    val role: RentalRole,
    val rentalDetailStatusModel: RentalDetailStatusModel
)