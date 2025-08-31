package com.example.rentit.domain.chat.model

data class ChatMessageModel (
    val messageId: String,
    val isMine: Boolean,
    val message: String,
    val sentAt: String,
)
