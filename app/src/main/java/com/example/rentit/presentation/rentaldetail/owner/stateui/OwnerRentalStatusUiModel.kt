package com.example.rentit.presentation.rentaldetail.owner.stateui

import com.example.rentit.common.enums.RentalStatus
import com.example.rentit.common.model.RentalSummaryUiModel
import com.example.rentit.presentation.rentaldetail.model.RentingStatus


sealed class OwnerRentalStatusUiModel {

    data class Request(
        val status: RentalStatus,
        val isPending: Boolean,
        val isAccepted: Boolean,
        val rentalSummary: RentalSummaryUiModel,
        val basicRentalFee: Int,
    ): OwnerRentalStatusUiModel()

    data class Paid(
        val status: RentalStatus,
        val daysUntilRental: Int,
        val rentalSummary: RentalSummaryUiModel,
        val basicRentalFee: Int,
        val isSendingPhotoRegistered: Boolean,
        val isSendingTrackingNumRegistered: Boolean,
        val rentalTrackingNumber: String?
    ): OwnerRentalStatusUiModel()

    data class Renting(
        val status: RentingStatus,
        val isOverdue: Boolean,
        val daysFromReturnDate: Int,
        val rentalSummary: RentalSummaryUiModel,
        val basicRentalFee: Int,
        val deposit: Int,
        val rentalTrackingNumber: String?
    ): OwnerRentalStatusUiModel()

    data class Returned(
        val status: RentalStatus,
        val rentalSummary: RentalSummaryUiModel,
        val basicRentalFee: Int,
        val deposit: Int,
        val rentalTrackingNumber: String?,
        val returnTrackingNumber: String?
    ): OwnerRentalStatusUiModel()

    data object Unknown: OwnerRentalStatusUiModel()
}
