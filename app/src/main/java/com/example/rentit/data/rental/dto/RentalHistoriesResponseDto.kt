package com.example.rentit.data.rental.dto

import com.example.rentit.common.enums.RentalStatus
import com.example.rentit.data.product.dto.ProductDto
import com.google.gson.annotations.SerializedName

data class RentalHistoriesResponseDto(
    @SerializedName("reservations")
    val reservations: List<RentalInfoDto>
)

data class RentalInfoDto(
    @SerializedName("reservationId")
    val reservationId: Int,

    @SerializedName("product")
    val product: ProductDto,

    @SerializedName("startDate")
    val startDate: String,

    @SerializedName("endDate")
    val endDate: String,

    @SerializedName("status")
    val status: RentalStatus,

    @SerializedName("requestedAt")
    val requestedAt: String
)