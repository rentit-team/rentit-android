package com.example.rentit.presentation.productdetail.rentalhistory

sealed class RentalHistorySideEffect {
    data class NavigateToRentalDetail(val reservationId: Int) : RentalHistorySideEffect()
    data object ScrollToTop : RentalHistorySideEffect()
    data class CommonError(val throwable: Throwable) : RentalHistorySideEffect()
}