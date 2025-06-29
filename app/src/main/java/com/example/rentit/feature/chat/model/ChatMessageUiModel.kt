package com.example.rentit.feature.chat.model

data class ChatMessageUiModel (
    val isMine: Boolean,
    val message: String,
    val sentAt: String,
)
