package com.example.rentit.data.product.dto

import com.google.gson.annotations.SerializedName

data class ReservationResponseDto(
    @SerializedName("data")
    val data: ReservationDataDto
)

data class ReservationDataDto(
    @SerializedName("reservationId")
    val reservationId: Int,

    @SerializedName("totalAmount")
    val totalAmount: Int,
)