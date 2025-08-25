package com.example.rentit.common.model

import kotlinx.serialization.Serializable

@Serializable
data class RentalSummaryUiModel(
    val productTitle: String,
    val thumbnailImgUrl: String?,
    val startDate: String,
    val endDate: String,
    val totalPrice: Int
)