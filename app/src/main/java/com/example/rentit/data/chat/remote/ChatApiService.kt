package com.example.rentit.data.chat.remote

import com.example.rentit.data.chat.dto.ChatListResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface ChatApiService {
    @GET("api/v1/chatrooms")
    @Headers("Content-Type: application/json")
    suspend fun getChatList(): Response<ChatListResponseDto>
}