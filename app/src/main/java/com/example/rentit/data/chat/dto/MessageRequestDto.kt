package com.example.rentit.data.chat.dto

data class MessageRequestDto(
    val chatroomId: String,
    val senderId: Long,
    val message: String
)