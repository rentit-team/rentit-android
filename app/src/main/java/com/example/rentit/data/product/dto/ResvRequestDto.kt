package com.example.rentit.data.product.dto

import com.google.gson.annotations.SerializedName

data class ResvRequestDto(

    @SerializedName("startDate")
    val startDate: String,

    @SerializedName("endDate")
    val endDate: String,
)