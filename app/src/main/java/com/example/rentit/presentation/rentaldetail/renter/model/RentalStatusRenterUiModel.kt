package com.example.rentit.presentation.rentaldetail.renter.model

import com.example.rentit.common.enums.RentalStatus


sealed class RentalStatusRenterUiModel {

    data class Request(
        val status: RentalStatus,
        val isAccepted: Boolean,
        val productTitle: String,
        val thumbnailImgUrl: String,
        val startDate: String,
        val endDate: String,
        val totalPrice: Int,
        val deposit: Int
    ): RentalStatusRenterUiModel()

    data class Paid(
        val status: RentalStatus,
        val daysUntilRental: Int,
        val productTitle: String,
        val thumbnailImgUrl: String,
        val startDate: String,
        val endDate: String,
        val totalPrice: Int,
        val deposit: Int,
        val rentalTrackingNumber: String?
    ): RentalStatusRenterUiModel()

    data class Renting(
        val status: RentingStatus,
        val daysFromReturnDate: Int,
        val productTitle: String,
        val thumbnailImgUrl: String,
        val startDate: String,
        val endDate: String,
        val totalPrice: Int,
        val deposit: Int,
        val rentalTrackingNumber: String?
    ): RentalStatusRenterUiModel()

    data class Returned(
        val status: RentalStatus,
        val productTitle: String,
        val thumbnailImgUrl: String,
        val startDate: String,
        val endDate: String,
        val totalPrice: Int,
        val deposit: Int,
        val rentalTrackingNumber: String?
    ): RentalStatusRenterUiModel()

    data object Unknown: RentalStatusRenterUiModel()
}
