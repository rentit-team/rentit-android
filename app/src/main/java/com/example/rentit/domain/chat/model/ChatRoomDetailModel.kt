package com.example.rentit.domain.chat.model

data class ChatRoomDetailModel (
    val rentalSummary: ChatRoomRentalSummaryModel,
    val productSummary: ChatRoomProductSummaryModel,
    val reservationId: Int,
    val productId: Int,
    val partnerNickname: String,
    val messages: List<ChatMessageModel>
)
