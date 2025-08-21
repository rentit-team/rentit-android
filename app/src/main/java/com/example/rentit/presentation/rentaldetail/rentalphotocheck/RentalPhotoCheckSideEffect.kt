package com.example.rentit.presentation.rentaldetail.rentalphotocheck

sealed class RentalPhotoCheckSideEffect {
    data object PopBackToRentalDetail : RentalPhotoCheckSideEffect()
}