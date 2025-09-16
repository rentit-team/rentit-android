package com.example.rentit.domain.chat.websocket

import com.example.rentit.data.chat.dto.MessageResponseDto

interface WebSocketManager {
    fun connect(chatRoomId: String, onMessageReceived: (MessageResponseDto) -> Unit, onError: (Throwable) -> Unit)

    fun sendMessage(chatRoomId: String, message: String, onSuccess: () -> Unit, onError: (Throwable) -> Unit)

    fun disconnect()
}