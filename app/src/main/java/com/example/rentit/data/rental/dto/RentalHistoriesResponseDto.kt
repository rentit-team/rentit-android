package com.example.rentit.data.rental.dto

import com.example.rentit.common.enums.RentalStatus
import com.google.gson.annotations.SerializedName

data class RentalHistoriesResponseDto(
    @SerializedName("reservations")
    val reservations: List<RentalHistoryDto>
)

data class RentalHistoryDto(
    @SerializedName("reservationId")
    val reservationId: Int,

    @SerializedName("renterNickName")
    val renterNickName: String,

    @SerializedName("startDate")
    val startDate: String,

    @SerializedName("endDate")
    val endDate: String,

    @SerializedName("status")
    val status: RentalStatus,

    @SerializedName("requestedAt")
    val requestedAt: String
)