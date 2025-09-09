package com.example.rentit.presentation.rentaldetail.rentalphotocheck

sealed class RentalPhotoCheckSideEffect {
    data class CommonError(val throwable: Throwable) : RentalPhotoCheckSideEffect()
}