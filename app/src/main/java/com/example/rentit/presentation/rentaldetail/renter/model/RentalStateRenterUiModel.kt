package com.example.rentit.presentation.rentaldetail.renter.model


sealed class RentalStatusRenterUiModel {

    data class Request(
        val status: String,
        val productTitle: String,
        val startDate: String,
        val endDate: String,
        val totalPrice: Int,
        val deposit: Int
    ): RentalStatusRenterUiModel()

    data object Paid: RentalStatusRenterUiModel()

    data object Renting: RentalStatusRenterUiModel()

    data object Returned: RentalStatusRenterUiModel()

    data object Unknown: RentalStatusRenterUiModel()
}
