package com.example.rentit.domain.chat.websocket

import com.example.rentit.domain.chat.model.ChatMessageModel

interface WebSocketManager {
    fun connect(chatroomId: String, onConnect: () -> Unit, onMessageReceived: (ChatMessageModel) -> Unit)

    fun sendMessage(chatroomId: String, message: String)

    fun disconnect()
}