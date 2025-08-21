package com.example.rentit.data.rental.dto

import com.google.gson.annotations.SerializedName

data class PhotoRegistrationResponseDto(
    @SerializedName("uploadedCount")
    val uploadedCount: Int,

    @SerializedName("photoUrls")
    val photoUrls: List<String>
)