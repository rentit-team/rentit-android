package com.example.rentit.domain.chat.repository

import com.example.rentit.data.chat.dto.ChatDetailResponseDto
import com.example.rentit.data.chat.dto.ChatListResponseDto
import com.example.rentit.data.chat.dto.MessageResponseDto
import com.example.rentit.data.chat.dto.NewChatResponseDto

interface ChatRepository {
    suspend fun getChatList(): Result<ChatListResponseDto>

    suspend fun getChatDetail(chatRoomId: String, skip: Int, size: Int): Result<ChatDetailResponseDto>

    suspend fun postNewChat(productId: Int): Result<NewChatResponseDto>

    fun connectWebSocket(chatRoomId: String, onMessageReceived: (MessageResponseDto) -> Unit, onError: (Throwable) -> Unit)

    fun sendMessage(chatRoomId: String, message: String, onSuccess: () -> Unit, onError: (Throwable) -> Unit)

    fun disconnectWebSocket()
}