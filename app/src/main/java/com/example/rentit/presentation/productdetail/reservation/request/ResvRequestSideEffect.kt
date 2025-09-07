package com.example.rentit.presentation.productdetail.reservation.request

sealed class ResvRequestSideEffect {
    data class NavigateToResvRequestComplete(
        val rentalStartDate: String,
        val rentalEndDate: String,
        val totalPrice: Int
    ): ResvRequestSideEffect()
    data object ToastInvalidPeriod: ResvRequestSideEffect()
}