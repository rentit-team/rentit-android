package com.example.rentit.presentation.rentaldetail.photobeforerent

sealed class PhotoBeforeRentSideEffect {
    data object PopBackToRentalDetail : PhotoBeforeRentSideEffect()
    data object ToastUploadSuccess : PhotoBeforeRentSideEffect()
    data object ToastUploadFailed : PhotoBeforeRentSideEffect()
}