package com.example.rentit.data.chat.remote

import com.example.rentit.data.chat.dto.ChatDetailResponseDto
import com.example.rentit.data.chat.dto.ChatListResponseDto
import com.example.rentit.data.chat.dto.NewChatResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface ChatApiService {
    @GET("api/v1/chatrooms")
    @Headers("Content-Type: application/json")
    suspend fun getChatList(): Response<ChatListResponseDto>

    @GET("api/v1/chatrooms/{chatroomId}/chats")
    @Headers("Content-Type: application/json")
    suspend fun getChatDetail(@Path("chatroomId") chatRoomId: String): Response<ChatDetailResponseDto>

    @POST("api/v1/products/{productId}/reservations/chatrooms")
    @Headers("Content-Type: application/json")
    suspend fun postNewChat(@Path("productId") productId: Int): Response<NewChatResponseDto>
}