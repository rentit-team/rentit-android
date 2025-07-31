package com.example.rentit.data.product.dto

// 백엔드 대여 상세 조회 API 구현 전 임시 DTO

data class RentalDetailDto(
    val rentalId: Long,
    val renterId: Long,
    val renterNickname: String,
    val sellerId: Long,
    val rentalStatus: String,
    val productImageUrl: String?,
    val productTitle: String,
    val startDate: String,
    val endDate: String,
    val pricePerDay: Int,
    val rentalTrackingNumber: String?,
    val returnTrackingNumber: String?,
    val isShipped: Boolean,
    val isReturned: Boolean,
    val overdueDays: Int
)
