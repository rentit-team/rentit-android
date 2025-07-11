package com.example.rentit.data.product.dto

import com.example.rentit.common.enums.ResvStatus
import com.google.gson.annotations.SerializedName

data class UpdateResvStatusRequestDto(
    @SerializedName("status")
    val status: ResvStatus,
)