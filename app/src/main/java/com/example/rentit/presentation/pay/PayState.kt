package com.example.rentit.presentation.pay

import com.example.rentit.common.model.RentalSummaryUiModel

data class PayState (
    val rentalSummary: RentalSummaryUiModel = RentalSummaryUiModel.EMPTY,
    val depositAmount: Int = 0,
    val showLoadErrorDialog: Boolean = false,
    val showPayResultDialog: Boolean = false,
    val isLoading: Boolean = false
) {
    val basicRentalFee: Int
        get() = rentalSummary.totalPrice - depositAmount
}