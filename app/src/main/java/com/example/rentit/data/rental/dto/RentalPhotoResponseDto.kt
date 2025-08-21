package com.example.rentit.data.rental.dto

import com.google.gson.annotations.SerializedName

data class RentalPhotoResponseDto(
    @SerializedName("rentalBefore")
    val rentalBefore: List<PhotoInfoDto>,

    @SerializedName("returnBefore")
    val returnBefore: List<PhotoInfoDto>,
)

data class PhotoInfoDto(
    @SerializedName("photoId")
    val photoId: Int,
    @SerializedName("url")
    val url: String,
    @SerializedName("uploadedAt")
    val uploadedAt: String,
)
