package com.example.rentit.presentation.rentaldetail.renter.stateui

import com.example.rentit.common.enums.RentalStatus
import com.example.rentit.common.uimodel.RentalSummaryUiModel
import com.example.rentit.presentation.rentaldetail.model.RentingStatus


sealed class RenterRentalStatusUiModel {

    data class Request(
        val status: RentalStatus,
        val isAccepted: Boolean,
        val rentalSummary: RentalSummaryUiModel,
        val basicRentalFee: Int,
        val deposit: Int,
    ): RenterRentalStatusUiModel()

    data class Paid(
        val status: RentalStatus,
        val daysUntilRental: Int,
        val rentalSummary: RentalSummaryUiModel,
        val basicRentalFee: Int,
        val deposit: Int,
        val rentalTrackingNumber: String?
    ): RenterRentalStatusUiModel()

    data class Renting(
        val status: RentingStatus,
        val isOverdue: Boolean,
        val isReturnAvailable: Boolean,
        val daysFromReturnDate: Int,
        val rentalSummary: RentalSummaryUiModel,
        val basicRentalFee: Int,
        val deposit: Int,
        val isReturnPhotoRegistered: Boolean,
        val isReturnTrackingNumRegistered: Boolean,
        val rentalTrackingNumber: String?
    ): RenterRentalStatusUiModel()

    data class Returned(
        val status: RentalStatus,
        val rentalSummary: RentalSummaryUiModel,
        val basicRentalFee: Int,
        val deposit: Int,
        val rentalTrackingNumber: String?,
        val returnTrackingNumber: String?
    ): RenterRentalStatusUiModel()

    data object Unknown: RenterRentalStatusUiModel()
}
