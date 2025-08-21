package com.example.rentit.data.rental.dto

import com.example.rentit.common.enums.RentalStatus
import com.google.gson.annotations.SerializedName

data class UpdateRentalStatusRequestDto(
    @SerializedName("status")
    val status: RentalStatus,
)