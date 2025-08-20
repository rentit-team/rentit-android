package com.example.rentit.presentation.rentaldetail.owner

sealed class OwnerRentalDetailSideEffect {
    data object NavigateToPhotoBeforeRent: OwnerRentalDetailSideEffect()
    data object NavigateToRentalPhotoCheck: OwnerRentalDetailSideEffect()
    data object ToastErrorGetCourierNames: OwnerRentalDetailSideEffect()
}