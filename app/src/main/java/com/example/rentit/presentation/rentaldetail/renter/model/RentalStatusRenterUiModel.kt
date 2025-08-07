package com.example.rentit.presentation.rentaldetail.renter.model

import com.example.rentit.common.enums.RentalStatus
import com.example.rentit.presentation.rentaldetail.common.model.RentalSummaryUiModel


sealed class RentalStatusRenterUiModel {

    data class Request(
        val status: RentalStatus,
        val isAccepted: Boolean,
        val rentalSummary: RentalSummaryUiModel,
        val basicRentalFee: Int,
        val deposit: Int,
    ): RentalStatusRenterUiModel()

    data class Paid(
        val status: RentalStatus,
        val daysUntilRental: Int,
        val rentalSummary: RentalSummaryUiModel,
        val basicRentalFee: Int,
        val deposit: Int,
        val rentalTrackingNumber: String?
    ): RentalStatusRenterUiModel()

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
    ): RentalStatusRenterUiModel()

    data class Returned(
        val status: RentalStatus,
        val rentalSummary: RentalSummaryUiModel,
        val basicRentalFee: Int,
        val deposit: Int,
        val rentalTrackingNumber: String?,
        val returnTrackingNumber: String?
    ): RentalStatusRenterUiModel()

    data object Unknown: RentalStatusRenterUiModel()
}
