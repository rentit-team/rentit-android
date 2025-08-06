package com.example.rentit.presentation.rentaldetail.common.model

data class RentalSummaryUiModel(
    val productTitle: String,
    val thumbnailImgUrl: String,
    val startDate: String,
    val endDate: String,
    val totalPrice: Int
)