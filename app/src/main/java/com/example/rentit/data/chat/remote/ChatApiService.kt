package com.example.rentit.data.chat.remote

import com.example.rentit.data.chat.dto.ChatDetailResponseDto
import com.example.rentit.data.chat.dto.ChatListResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface ChatApiService {
    @GET("api/v1/chatrooms")
    @Headers("Content-Type: application/json")
    suspend fun getChatList(): Response<ChatListResponseDto>

    @GET("api/v1/chatrooms/{chatroomId}/chats")
    @Headers("Content-Type: application/json")
    suspend fun getChatDetail(@Path("chatroomId") chatRoomId: String): Response<ChatDetailResponseDto>
}