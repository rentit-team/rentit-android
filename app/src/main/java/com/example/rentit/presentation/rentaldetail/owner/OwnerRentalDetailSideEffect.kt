package com.example.rentit.presentation.rentaldetail.owner

sealed class OwnerRentalDetailSideEffect {
    data object NavigateToPhotoBeforeRent: OwnerRentalDetailSideEffect()
    data object NavigateToRentalPhotoCheck: OwnerRentalDetailSideEffect()
    data object NavigateBack: OwnerRentalDetailSideEffect()
    data object ToastErrorGetCourierNames: OwnerRentalDetailSideEffect()
    data object ToastSuccessTrackingRegistration: OwnerRentalDetailSideEffect()
    data object ToastErrorTrackingRegistration: OwnerRentalDetailSideEffect()
    data object ToastCancelRentalSuccess: OwnerRentalDetailSideEffect()
    data object ToastCancelRentalFailed: OwnerRentalDetailSideEffect()
    data object ToastAcceptRentalSuccess: OwnerRentalDetailSideEffect()
    data object ToastAcceptRentalFailed: OwnerRentalDetailSideEffect()
}