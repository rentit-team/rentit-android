package com.example.rentit.presentation.rentaldetail.owner

import com.example.rentit.common.model.RequestAcceptDialogUiModel

data class OwnerRentalDetailState(
    val isLoading: Boolean = false,
    val selectedCourierName: String? = null,
    val trackingNumber: String = "",
    val requestAcceptDialog: RequestAcceptDialogUiModel? = null,
    val showCancelDialog: Boolean = false,
    val trackingRegDialog: List<String> = emptyList(),
    val showUnknownStatusDialog: Boolean = false,
)