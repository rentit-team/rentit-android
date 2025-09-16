package com.example.rentit.domain.rental.model

import com.example.rentit.common.enums.RentalStatus
import com.example.rentit.common.enums.RentingStatus
import com.example.rentit.common.uimodel.RentalSummaryUiModel


sealed class RentalDetailStatusModel {

    data class Request(
        val status: RentalStatus,
        val rentalSummary: RentalSummaryUiModel,
        val basicRentalFee: Int,
        val deposit: Int,
        val isPending: Boolean,
        val isAccepted: Boolean
    ): RentalDetailStatusModel()

    data class Paid(
        val status: RentalStatus,
        val rentalSummary: RentalSummaryUiModel,
        val daysUntilRental: Int,
        val basicRentalFee: Int,
        val deposit: Int,
        val rentalTrackingNumber: String?,
        val rentalCourierName: String?,
        val isSendingPhotoRegistered: Boolean,
        val isSendingTrackingNumRegistered: Boolean
    ): RentalDetailStatusModel()

    data class Renting(
        val status: RentingStatus,
        val rentalSummary: RentalSummaryUiModel,
        val daysFromReturnDate: Int,
        val basicRentalFee: Int,
        val deposit: Int,
        val rentalTrackingNumber: String?,
        val rentalCourierName: String?,
        val returnTrackingNumber: String?,
        val returnCourierName: String?,
        val isOverdue: Boolean,
        val isReturnAvailable: Boolean,
        val isReturnPhotoRegistered: Boolean,
        val isReturnTrackingNumRegistered: Boolean
    ): RentalDetailStatusModel()

    data class Returned(
        val status: RentalStatus,
        val rentalSummary: RentalSummaryUiModel,
        val basicRentalFee: Int,
        val deposit: Int,
        val rentalTrackingNumber: String?,
        val rentalCourierName: String?,
        val returnTrackingNumber: String?,
        val returnCourierName: String?
    ): RentalDetailStatusModel()

    data object Unknown: RentalDetailStatusModel()
}
