package com.example.rentit.presentation.rentaldetail.photobeforereturn

sealed class PhotoBeforeReturnSideEffect {
    data object PopBackToRentalDetail : PhotoBeforeReturnSideEffect()
    data object ToastUploadSuccess : PhotoBeforeReturnSideEffect()
    data object ToastUploadFailed : PhotoBeforeReturnSideEffect()
    data class CommonError(val throwable: Throwable) : PhotoBeforeReturnSideEffect()
}