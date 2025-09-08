package com.example.rentit.data.chat.repositoryImpl

import com.example.rentit.core.network.getOrThrow
import com.example.rentit.data.chat.dto.ChatDetailResponseDto
import com.example.rentit.data.chat.dto.ChatListResponseDto
import com.example.rentit.data.chat.dto.NewChatResponseDto
import com.example.rentit.data.chat.remote.ChatRemoteDataSource
import com.example.rentit.domain.chat.repository.ChatRepository
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val chatRemoteDataSource: ChatRemoteDataSource,
): ChatRepository {

    override suspend fun getChatList(): Result<ChatListResponseDto> {
        val response = chatRemoteDataSource.getChatList()
        return response.getOrThrow()
    }

    override suspend fun getChatDetail(chatRoomId: String, skip: Int, size: Int): Result<ChatDetailResponseDto> {
        val response = chatRemoteDataSource.getChatDetail(chatRoomId, skip, size)
        return response.getOrThrow()
    }

    override suspend fun postNewChat(productId: Int): Result<NewChatResponseDto> {
        val response = chatRemoteDataSource.postNewChat(productId)
        return response.getOrThrow()
    }
}