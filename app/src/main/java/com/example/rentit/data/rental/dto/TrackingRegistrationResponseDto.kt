package com.example.rentit.data.rental.dto

import com.google.gson.annotations.SerializedName

data class TrackingRegistrationResponseDto(
    @SerializedName("message")
    val message: String,

    @SerializedName("tracking")
    val tracking: TrackingInfoDto
)

data class TrackingInfoDto(
    @SerializedName("type")
    val type: String,

    @SerializedName("courierName")
    val courierName: String,

    @SerializedName("trackingNumber")
    val trackingNumber: String,

    @SerializedName("registeredAt")
    val registeredAt: String,
)