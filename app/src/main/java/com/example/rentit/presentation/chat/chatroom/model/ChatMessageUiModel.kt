package com.example.rentit.presentation.chat.chatroom.model

data class ChatMessageUiModel (
    val isMine: Boolean,
    val message: String,
    val sentAt: String,
)
