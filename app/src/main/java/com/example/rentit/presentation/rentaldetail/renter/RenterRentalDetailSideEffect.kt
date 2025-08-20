package com.example.rentit.presentation.rentaldetail.renter

sealed class RenterRentalDetailSideEffect {
    data object NavigateToPay: RenterRentalDetailSideEffect()
    data object NavigateToPhotoBeforeReturn: RenterRentalDetailSideEffect()
    data object NavigateToRentalPhotoCheck: RenterRentalDetailSideEffect()
    data object ToastErrorGetCourierNames: RenterRentalDetailSideEffect()
    data object ToastSuccessTrackingRegistration: RenterRentalDetailSideEffect()
    data object ToastErrorTrackingRegistration: RenterRentalDetailSideEffect()
}