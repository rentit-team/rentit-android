package com.example.rentit.domain.chat.model

data class ChatRoomDetailModel (
    val reservationId: Int,
    val productId: Int,
    val partnerNickname: String,
    val messages: List<ChatMessageModel>
)
