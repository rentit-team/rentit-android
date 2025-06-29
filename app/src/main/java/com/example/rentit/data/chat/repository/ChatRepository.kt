package com.example.rentit.data.chat.repository

import android.util.Log
import com.example.rentit.common.exception.chat.ForbiddenChatAccessException
import com.example.rentit.common.exception.ServerException
import com.example.rentit.common.exception.chat.ChatRoomAlreadyExistsException
import com.example.rentit.common.exception.product.ReservationByOwnerException
import com.example.rentit.data.chat.dto.ChatDetailResponseDto
import com.example.rentit.data.chat.dto.ChatListResponseDto
import com.example.rentit.data.chat.dto.NewChatResponseDto
import com.example.rentit.data.chat.remote.ChatRemoteDataSource
import javax.inject.Inject

private const val TAG = "ChatRepository"

class ChatRepository @Inject constructor(
    private val chatRemoteDataSource: ChatRemoteDataSource,
) {
    suspend fun getChatList(): Result<ChatListResponseDto> {
        return runCatching {
            val response = chatRemoteDataSource.getChatList()
            when (response.code()) {
                200 -> response.body() ?: throw Exception("Empty response body")
                500 -> {
                    Log.e(TAG, "Server error 500: ${response.errorBody()?.string()}")
                    throw Exception("Server error")
                }
                else -> {
                    Log.e(TAG, "Unexpected error: code=${response.code()}, ${response.errorBody()?.string()}")
                    throw Exception("Unexpected error")
                }
            }
        }
    }

    suspend fun getChatDetail(chatRoomMessageId: String, skip: Int, size: Int): Result<ChatDetailResponseDto> {
        return runCatching {
            val response = chatRemoteDataSource.getChatDetail(chatRoomMessageId, skip, size)
            when (response.code()) {
                200 -> response.body() ?: throw Exception("Empty response body")
                403 -> {
                    Log.e(TAG, "Server error 403: ${response.errorBody()?.string()}")
                    throw ForbiddenChatAccessException()
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

    suspend fun postNewChat(productId: Int): Result<NewChatResponseDto> {
        return runCatching {
            val response = chatRemoteDataSource.postNewChat(productId)
            when (response.code()) {
                200 -> response.body() ?: throw Exception("Empty response body")
                403 -> {
                    Log.e(TAG, "Server error 403: ${response.errorBody()?.string()}")
                    throw ReservationByOwnerException()
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