package com.example.rentit.presentation.rentaldetail.owner

import com.example.rentit.common.model.RequestAcceptDialogUiModel

data class OwnerRentalDetailState(
    val isLoading: Boolean = false,
    val selectedCourierName: String = "",
    val trackingNumber: String = "",
    val trackingCourierNames: List<String> = emptyList(),
    val showTrackingNumberEmptyError: Boolean = false,
    val showTrackingRegDialog: Boolean = false,
    val requestAcceptDialog: RequestAcceptDialogUiModel? = null,
    val showCancelDialog: Boolean = false,
    val showUnknownStatusDialog: Boolean = false,
)