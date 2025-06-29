package com.example.rentit.data.product.dto

import com.example.rentit.common.enums.BookingStatus
import com.google.gson.annotations.SerializedName

data class UpdateBookingStatusRequestDto(
    @SerializedName("status")
    val status: BookingStatus,
)