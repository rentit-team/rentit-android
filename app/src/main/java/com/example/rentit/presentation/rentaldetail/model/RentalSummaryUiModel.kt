package com.example.rentit.presentation.rentaldetail.model

import kotlinx.serialization.Serializable

@Serializable
data class RentalSummaryUiModel(
    val productTitle: String,
    val thumbnailImgUrl: String?,
    val startDate: String,
    val endDate: String,
    val totalPrice: Int
) {
    companion object {
        val EMPTY = RentalSummaryUiModel(
            productTitle = "",
            thumbnailImgUrl = null,
            startDate = "",
            endDate = "",
            totalPrice = 0
        )
    }
}