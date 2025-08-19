package com.example.rentit.data.rental.dto

import com.google.gson.annotations.SerializedName

data class RentalDetailResponseDto(
    @SerializedName("rental")
    val rental: RentalDto,

    @SerializedName("statusHistory")
    val statusHistory: List<StatusHistoryDto>
)

data class RentalDto(
    @SerializedName("reservationId")
    val reservationId: Long,

    @SerializedName("renter")
    val renter: RenterDto,

    @SerializedName("status")
    val status: String,

    @SerializedName("product")
    val product: ProductDto,

    @SerializedName("startDate")
    val startDate: String,

    @SerializedName("endDate")
    val endDate: String,

    @SerializedName("totalAmount")
    val totalAmount: Int,

    @SerializedName("depositAmount")
    val depositAmount: Int,

    @SerializedName("rentalTrackingNumber")
    val rentalTrackingNumber: String?,

    @SerializedName("returnTrackingNumber")
    val returnTrackingNumber: String?,

    @SerializedName("deliveryStatus")
    val deliveryStatus: DeliveryStatusDto,

    @SerializedName("returnStatus")
    val returnStatus: ReturnStatusDto
)

data class RenterDto(
    @SerializedName("userId")
    val userId: Long,

    @SerializedName("nickname")
    val nickname: String
)

data class ProductDto(
    @SerializedName("title")
    val title: String,

    @SerializedName("thumbnailImgUrl")
    val thumbnailImgUrl: String?
)

data class DeliveryStatusDto(
    @SerializedName("isPhotoRegistered")
    val isPhotoRegistered: Boolean,

    @SerializedName("isTrackingNumberRegistered")
    val isTrackingNumberRegistered: Boolean
)

data class ReturnStatusDto(
    @SerializedName("isPhotoRegistered")
    val isPhotoRegistered: Boolean,

    @SerializedName("isTrackingNumberRegistered")
    val isTrackingNumberRegistered: Boolean
)

data class StatusHistoryDto(
    @SerializedName("status")
    val status: String,

    @SerializedName("changedAt")
    val changedAt: String,

    @SerializedName("changedBy")
    val changedBy: ChangedByDto
)

data class ChangedByDto(
    @SerializedName("userId")
    val userId: Long,

    @SerializedName("nickname")
    val nickname: String
)
