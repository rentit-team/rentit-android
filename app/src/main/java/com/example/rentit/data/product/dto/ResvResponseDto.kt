package com.example.rentit.data.product.dto

import com.google.gson.annotations.SerializedName

data class ResvResponseDto(
    @SerializedName("data")
    val data: ResvDataDto
)

data class ResvDataDto(
    @SerializedName("reservationId")
    val reservationId: Int,

    @SerializedName("totalAmount")
    val totalAmount: Int,
)