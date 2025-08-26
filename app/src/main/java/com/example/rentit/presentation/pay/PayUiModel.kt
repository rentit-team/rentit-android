package com.example.rentit.presentation.pay

import com.example.rentit.common.model.RentalSummaryUiModel
import kotlinx.serialization.Serializable

@Serializable
data class PayUiModel(
    val rentalSummary: RentalSummaryUiModel,
    val basicRentalFee: Int,
    val deposit: Int
)