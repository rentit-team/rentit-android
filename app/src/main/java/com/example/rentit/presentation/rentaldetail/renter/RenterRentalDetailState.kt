package com.example.rentit.presentation.rentaldetail.renter

data class RenterRentalDetailState(
    val isLoading: Boolean = false,
    val selectedCourierName: String = "",
    val trackingNumber: String = "",
    val trackingCourierNames: List<String> = emptyList(),
    val showTrackingNumberEmptyError: Boolean = false,
    val showCancelDialog: Boolean = false,
    val showUnknownStatusDialog: Boolean = false,
)