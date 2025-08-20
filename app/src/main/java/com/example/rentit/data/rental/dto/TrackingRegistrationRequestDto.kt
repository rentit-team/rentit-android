package com.example.rentit.data.rental.dto

import com.google.gson.annotations.SerializedName

data class TrackingRegistrationRequestDto(
    @SerializedName("type")
    val type: String,

    @SerializedName("courierName")
    val courierName: String,

    @SerializedName("trackingNumber")
    val trackingNumber: String,
)