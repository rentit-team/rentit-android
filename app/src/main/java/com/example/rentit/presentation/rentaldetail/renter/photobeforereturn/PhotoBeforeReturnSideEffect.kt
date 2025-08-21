package com.example.rentit.presentation.rentaldetail.renter.photobeforereturn

sealed class PhotoBeforeReturnSideEffect {
    data object PopBackToRentalDetail : PhotoBeforeReturnSideEffect()
    data object ToastUploadSuccess : PhotoBeforeReturnSideEffect()
    data object ToastUploadFailed : PhotoBeforeReturnSideEffect()
}