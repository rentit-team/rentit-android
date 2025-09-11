package com.example.rentit.presentation.productdetail.reservation

sealed class ReservationSideEffect {
    data class NavigateToReservationComplete(
        val rentalStartDate: String,
        val rentalEndDate: String,
        val totalPrice: Int
    ): ReservationSideEffect()
    data object ToastInvalidPeriod: ReservationSideEffect()
    data object ToastPostReservationFailed: ReservationSideEffect()
    data class CommonError(val throwable: Throwable): ReservationSideEffect()
}