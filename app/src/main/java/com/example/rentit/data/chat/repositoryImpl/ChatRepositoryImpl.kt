package com.example.rentit.data.chat.repositoryImpl

import com.example.rentit.core.network.safeApiCall
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
        return safeApiCall(chatRemoteDataSource::getChatList)
    }

    override suspend fun getChatDetail(chatRoomId: String, skip: Int, size: Int): Result<ChatDetailResponseDto> {
        return safeApiCall { chatRemoteDataSource.getChatDetail(chatRoomId, skip, size) }
    }

    override suspend fun postNewChat(productId: Int): Result<NewChatResponseDto> {
        return safeApiCall { chatRemoteDataSource.postNewChat(productId) }
    }
}