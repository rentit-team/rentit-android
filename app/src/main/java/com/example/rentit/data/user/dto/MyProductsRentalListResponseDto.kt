package com.example.rentit.data.user.dto

import com.example.rentit.common.enums.RentalStatus
import com.google.gson.annotations.SerializedName

data class MyProductsRentalListResponseDto(
    @SerializedName("rentalHistory")
    val rentalHistory: List<MyProductsRentalDto>,
)

data class MyProductsRentalDto(
    @SerializedName("productId")
    val productId: Int,

    @SerializedName("reservationId")
    val reservationId: Int,

    @SerializedName("productTitle")
    val productTitle: String,

    @SerializedName("productThumbnailImgUrl")
    val productThumbnailImgUrl: String?,

    @SerializedName("startDate")
    val startDate: String, // "YYYY-MM-DD"

    @SerializedName("endDate")
    val endDate: String, // "YYYY-MM-DD"

    @SerializedName("status")
    val status: RentalStatus,

    @SerializedName("totalPrice")
    val totalPrice: Int,

    @SerializedName("renterNickname")
    val renterNickname: String
)
