package com.example.rentit.data.rental.dto

import com.google.gson.annotations.SerializedName

data class CourierNamesResponseDto(
    @SerializedName("courierNames")
    val courierNames: List<String>,
)