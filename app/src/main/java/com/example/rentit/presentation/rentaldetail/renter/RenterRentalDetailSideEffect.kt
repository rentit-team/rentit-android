package com.example.rentit.presentation.rentaldetail.renter

sealed class RenterRentalDetailSideEffect {
    data object NavigateToPay: RenterRentalDetailSideEffect()
    data object NavigateToPhotoBeforeReturn: RenterRentalDetailSideEffect()
    data object NavigateToRentalPhotoCheck: RenterRentalDetailSideEffect()
}