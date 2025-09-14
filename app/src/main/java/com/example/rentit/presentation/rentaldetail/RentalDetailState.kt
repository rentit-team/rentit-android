package com.example.rentit.presentation.rentaldetail

import com.example.rentit.common.enums.RentalRole
import com.example.rentit.common.enums.RentalProcessType
import com.example.rentit.common.uimodel.RequestAcceptDialogUiModel
import com.example.rentit.domain.rental.model.RentalDetailStatusModel

data class RentalDetailState(
    val role: RentalRole = RentalRole.DEFAULT,
    val rentalDetailStatusModel: RentalDetailStatusModel = RentalDetailStatusModel.Unknown,
    val selectedCourierName: String = "",
    val trackingNumber: String = "",
    val trackingCourierNames: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val showTransactionReceiptConfirmDialog: Boolean = false,
    val showTrackingNumberEmptyError: Boolean = false,
    val showTrackingRegDialog: Boolean = false,
    val requestAcceptDialog: RequestAcceptDialogUiModel? = null,
    val showCancelDialog: Boolean = false,
) {
    val trackingRegisterRequestType: RentalProcessType
        get() = when(rentalDetailStatusModel) {
            is RentalDetailStatusModel.Paid -> RentalProcessType.RENTAL
            is RentalDetailStatusModel.Renting -> RentalProcessType.RETURN
            else -> RentalProcessType.NONE
        }
}