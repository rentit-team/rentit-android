package com.example.rentit.domain.chat.websocket

import com.example.rentit.data.chat.dto.MessageResponseDto

interface WebSocketManager {
    fun connect(chatroomId: String, onConnect: () -> Unit, onMessageReceived: (MessageResponseDto) -> Unit)

    fun sendMessage(chatroomId: String, message: String)

    fun disconnect()
}