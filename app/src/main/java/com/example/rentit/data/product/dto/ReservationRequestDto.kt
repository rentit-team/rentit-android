package com.example.rentit.data.product.dto

import com.google.gson.annotations.SerializedName

data class ReservationRequestDto(

    @SerializedName("startDate")
    val startDate: String,

    @SerializedName("endDate")
    val endDate: String,
)