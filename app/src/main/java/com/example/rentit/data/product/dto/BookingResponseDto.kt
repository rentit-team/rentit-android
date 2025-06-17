package com.example.rentit.data.product.dto

import com.google.gson.annotations.SerializedName

data class BookingResponseDto(
    @SerializedName("data")
    val data: BookingDataDto
)

data class BookingDataDto(
    @SerializedName("reservationId")
    val reservationId: Int,

    @SerializedName("totalAmount")
    val totalAmount: Int,
)