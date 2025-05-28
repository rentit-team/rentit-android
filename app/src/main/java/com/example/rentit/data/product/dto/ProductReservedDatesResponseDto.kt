package com.example.rentit.data.product.dto

import com.google.gson.annotations.SerializedName

data class ProductReservedDatesResponseDto(
    @SerializedName("disabledDates")
    val disabledDates: List<String>
)