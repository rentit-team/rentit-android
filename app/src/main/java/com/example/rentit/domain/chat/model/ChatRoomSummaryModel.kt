package com.example.rentit.domain.chat.model

import java.time.OffsetDateTime

data class ChatRoomSummaryModel (
    val chatRoomId: String,
    val thumbnailImgUrl: String?,
    val productTitle: String,
    val partnerNickname: String,
    val lastMessage: String,
    val lastMessageTime: OffsetDateTime?,
)