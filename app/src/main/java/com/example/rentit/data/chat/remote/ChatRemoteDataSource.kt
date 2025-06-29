package com.example.rentit.data.chat.remote

import com.example.rentit.data.chat.dto.ChatDetailResponseDto
import com.example.rentit.data.chat.dto.ChatListResponseDto
import com.example.rentit.data.chat.dto.NewChatResponseDto
import retrofit2.Response
import javax.inject.Inject

class ChatRemoteDataSource @Inject constructor(
    private val chatApiService: ChatApiService
){
    suspend fun getChatList(): Response<ChatListResponseDto> {
        return chatApiService.getChatList()
    }
    suspend fun getChatDetail(chatRoomId: String, skip: Int, size: Int): Response<ChatDetailResponseDto> {
        return chatApiService.getChatDetail(chatRoomId, skip, size)
    }
    suspend fun postNewChat(productId: Int): Response<NewChatResponseDto> {
        return chatApiService.postNewChat(productId)
    }
}