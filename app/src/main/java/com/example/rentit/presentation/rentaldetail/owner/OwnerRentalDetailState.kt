package com.example.rentit.presentation.rentaldetail.owner

data class OwnerRentalDetailState(
    val isLoading: Boolean = false,
    val requestAcceptDialog: Boolean = false,
    val showCancelDialog: Boolean = false,
    val showTrackingRegDialog: Boolean = false,
    val showUnknownStatusDialog: Boolean = false,
)