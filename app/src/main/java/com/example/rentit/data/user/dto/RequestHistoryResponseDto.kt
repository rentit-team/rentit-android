package com.example.rentit.data.user.dto

import com.google.gson.annotations.SerializedName

data class RequestHistoryResponseDto(
    @SerializedName("reservations")
    val reservations: List<RequestInfoDto>,
)

data class RequestInfoDto(
    @SerializedName("reservationId")
    val reservationId: Int,

    @SerializedName("renterNickName")
    val renterNickName: String,

    @SerializedName("startDate")
    val startDate: String,

    @SerializedName("endDate")
    val endDate: String,

    @SerializedName("status")
    val status: String,

    @SerializedName("requestedAt")
    val requestedAt: String,
)