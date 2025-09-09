package com.example.rentit.presentation.pay

import com.example.rentit.common.uimodel.RentalSummaryUiModel

data class PayState (
    val rentalSummary: RentalSummaryUiModel = RentalSummaryUiModel.EMPTY,
    val depositAmount: Int = 0,
    val showPayResultDialog: Boolean = false,
    val isLoading: Boolean = false
) {
    val basicRentalFee: Int
        get() = rentalSummary.totalPrice - depositAmount
}