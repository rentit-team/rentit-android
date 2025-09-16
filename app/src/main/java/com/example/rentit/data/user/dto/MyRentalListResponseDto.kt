package com.example.rentit.data.user.dto

import com.example.rentit.common.enums.RentalStatus
import com.example.rentit.data.product.dto.ProductDto
import com.google.gson.annotations.SerializedName

data class MyRentalListResponseDto(
    @SerializedName("myReservations")
    val myReservations: List<ReservationDto>,

    @SerializedName("nearestDueItem")
    val nearestDueItem: NearestDueItemDto?
)

data class ReservationDto(
    @SerializedName("reservationId")
    val reservationId: Int,

    @SerializedName("product")
    val product: ProductDto,

    @SerializedName("startDate")
    val startDate: String, // "YYYY-MM-DD"

    @SerializedName("endDate")
    val endDate: String, // "YYYY-MM-DD"

    @SerializedName("status")
    val status: RentalStatus, // PENDING, REJECTED, ACCEPTED

    @SerializedName("requestedAt")
    val requestedAt: String // ISO 8601
)

data class NearestDueItemDto(
    @SerializedName("reservationId")
    val reservationId: Int,

    @SerializedName("productId")
    val productId: Int,

    @SerializedName("productTitle")
    val productTitle: String,

    @SerializedName("returnDueDate")
    val returnDueDate: String, // "YYYY-MM-DD"

    @SerializedName("daysUntilReturn")
    val daysUntilReturn: Int
)
