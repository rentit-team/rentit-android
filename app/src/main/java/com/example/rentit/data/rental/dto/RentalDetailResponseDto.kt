package com.example.rentit.data.rental.dto

import kotlinx.serialization.Serializable

@Serializable
data class RentalDetailResponseDto(
    val rental: Rental,
    val statusHistory: List<StatusHistory>
)

@Serializable
data class Rental(
    val reservationId: Int,
    val renter: Renter,
    val status: String,
    val product: Product,
    val startDate: String,
    val endDate: String,
    val totalAmount: Int,
    val depositAmount: Int,
    val rentalTrackingNumber: String? = null,
    val returnTrackingNumber: String? = null,
    val deliveryStatus: DeliveryStatus,
    val returnStatus: ReturnStatus
)

@Serializable
data class Renter(
    val userId: Int,
    val nickname: String
)

@Serializable
data class Product(
    val title: String,
    val thumbnailImgUrl: String
)

@Serializable
data class DeliveryStatus(
    val isPhotoRegistered: Boolean,
    val isTrackingNumberRegistered: Boolean
)

@Serializable
data class ReturnStatus(
    val isPhotoRegistered: Boolean,
    val isTrackingNumberRegistered: Boolean
)

@Serializable
data class StatusHistory(
    val status: String,
    val changedAt: String,
    val changedBy: ChangedBy
)

@Serializable
data class ChangedBy(
    val userId: Int? = null,
    val nickname: String? = null
)
