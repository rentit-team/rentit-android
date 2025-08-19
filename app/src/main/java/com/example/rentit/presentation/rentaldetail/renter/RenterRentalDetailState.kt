package com.example.rentit.presentation.rentaldetail.renter

data class RenterRentalDetailState(
    val isLoading: Boolean = false,
    val showCancelDialog: Boolean = false,
    val showTrackingRegDialog: Boolean = false,
    val showUnknownStatusDialog: Boolean = false,
)