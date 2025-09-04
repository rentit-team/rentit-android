package com.example.rentit.domain.chat.websocket

import com.example.rentit.data.chat.dto.MessageResponseDto

interface WebSocketManager {
    fun connect(chatroomId: String, onMessageReceived: (MessageResponseDto) -> Unit, onError: (Throwable) -> Unit)

    fun sendMessage(chatroomId: String, message: String, onError: (Throwable) -> Unit)

    fun disconnect()
}