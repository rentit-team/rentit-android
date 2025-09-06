package com.example.rentit.data.chat.repositoryImpl

import android.util.Log
import com.example.rentit.common.exception.MissingTokenException
import com.example.rentit.domain.chat.exception.ForbiddenChatAccessException
import com.example.rentit.common.exception.ServerException
import com.example.rentit.domain.chat.exception.ChatRoomAlreadyExistsException
import com.example.rentit.data.chat.dto.ChatDetailResponseDto
import com.example.rentit.data.chat.dto.ChatListResponseDto
import com.example.rentit.data.chat.dto.NewChatResponseDto
import com.example.rentit.data.chat.remote.ChatRemoteDataSource
import com.example.rentit.domain.chat.repository.ChatRepository
import com.example.rentit.domain.product.exception.AccessNotAllowedException
import com.example.rentit.domain.rental.exception.EmptyBodyException
import javax.inject.Inject

private const val TAG = "ChatRepository"

class ChatRepositoryImpl @Inject constructor(
    private val chatRemoteDataSource: ChatRemoteDataSource,
): ChatRepository {

    override suspend fun getChatList(): Result<ChatListResponseDto> {
        return runCatching {
            val response = chatRemoteDataSource.getChatList()
            if(response.isSuccessful){
                response.body() ?: throw EmptyBodyException()
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Client Error"
                throw if(response.code() == 401) MissingTokenException(errorMsg) else Exception(errorMsg)
            }
        }
    }

    override suspend fun getChatDetail(chatRoomId: String, skip: Int, size: Int): Result<ChatDetailResponseDto> {
        return runCatching {
            val response = chatRemoteDataSource.getChatDetail(chatRoomId, skip, size)
            val errorMsg = response.errorBody()?.string() ?: "Client Error"
            when(response.code()) {
                200 -> response.body() ?: throw EmptyBodyException()
                401 -> throw MissingTokenException(errorMsg)
                403 -> throw ForbiddenChatAccessException(errorMsg)
                else -> throw Exception(errorMsg)
            }
        }
    }

    override suspend fun postNewChat(productId: Int): Result<NewChatResponseDto> {
        return runCatching {
            val response = chatRemoteDataSource.postNewChat(productId)
            when (response.code()) {
                200 -> response.body() ?: throw Exception("Empty response body")
                403 -> {
                    Log.e(TAG, "Server error 403: ${response.errorBody()?.string()}")
                    throw AccessNotAllowedException()
                }
                409 -> {
                    Log.e(TAG, "Server error 409: ${response.errorBody()?.string()}")
                    throw ChatRoomAlreadyExistsException()
                }
                500 -> {
                    Log.e(TAG, "Server error 500: ${response.errorBody()?.string()}")
                    throw ServerException("Server error")
                }
                else -> {
                    Log.e(TAG, "Unexpected error: code=${response.code()}, ${response.errorBody()?.string()}")
                    throw Exception("Unexpected error")
                }
            }
        }
    }
}