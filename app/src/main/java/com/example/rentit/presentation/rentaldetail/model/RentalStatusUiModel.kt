package com.example.rentit.presentation.rentaldetail.model

import com.example.rentit.common.enums.RentalStatus
import com.example.rentit.common.uimodel.RentalSummaryUiModel


sealed class RentalStatusUiModel {

    data class Request(
        val status: RentalStatus,
        val rentalSummary: RentalSummaryUiModel,
        val basicRentalFee: Int,
        val deposit: Int,
        val isPending: Boolean,
        val isAccepted: Boolean
    ): RentalStatusUiModel()

    data class Paid(
        val status: RentalStatus,
        val rentalSummary: RentalSummaryUiModel,
        val daysUntilRental: Int,
        val basicRentalFee: Int,
        val deposit: Int,
        val rentalTrackingNumber: String?,
        val isSendingPhotoRegistered: Boolean,
        val isSendingTrackingNumRegistered: Boolean
    ): RentalStatusUiModel()

    data class Renting(
        val status: RentingStatus,
        val rentalSummary: RentalSummaryUiModel,
        val daysFromReturnDate: Int,
        val basicRentalFee: Int,
        val deposit: Int,
        val rentalTrackingNumber: String?,
        val isOverdue: Boolean,
        val isReturnAvailable: Boolean,
        val isReturnPhotoRegistered: Boolean,
        val isReturnTrackingNumRegistered: Boolean
    ): RentalStatusUiModel()

    data class Returned(
        val status: RentalStatus,
        val rentalSummary: RentalSummaryUiModel,
        val basicRentalFee: Int,
        val deposit: Int,
        val rentalTrackingNumber: String?,
        val returnTrackingNumber: String?
    ): RentalStatusUiModel()

    data object Unknown: RentalStatusUiModel()
}
