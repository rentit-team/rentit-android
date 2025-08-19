package com.example.rentit.presentation.rentaldetail.owner

import com.example.rentit.common.model.RequestAcceptDialogUiModel

data class OwnerRentalDetailState(
    val isLoading: Boolean = false,
    val requestAcceptDialog: RequestAcceptDialogUiModel? = null,
    val showCancelDialog: Boolean = false,
    val showTrackingRegDialog: Boolean = false,
    val showUnknownStatusDialog: Boolean = false,
)