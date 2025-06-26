package com.example.rentit.data.chat.remote

import com.example.rentit.data.chat.dto.ChatDetailResponseDto
import com.example.rentit.data.chat.dto.ChatListResponseDto
import retrofit2.Response
import javax.inject.Inject

class ChatRemoteDataSource @Inject constructor(
    private val chatApiService: ChatApiService
){
    suspend fun getChatList(): Response<ChatListResponseDto> {
        return chatApiService.getChatList()
    }
    suspend fun getChatDetail(chatRoomId: String): Response<ChatDetailResponseDto> {
        return chatApiService.getChatDetail(chatRoomId)
    }
}