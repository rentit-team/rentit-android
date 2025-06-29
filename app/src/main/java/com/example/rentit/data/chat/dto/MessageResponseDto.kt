package com.example.rentit.data.chat.dto

data class MessageResponseDto(
    val messageId: String,
    val senderId: Long,
    val content: String,
    val sentAt: String,
)