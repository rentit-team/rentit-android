package com.example.rentit.data.chat.repository

import android.util.Log
import com.example.rentit.common.exception.chat.ForbiddenChatAccessException
import com.example.rentit.common.exception.chat.ServerException
import com.example.rentit.data.chat.dto.ChatDetailResponseDto
import com.example.rentit.data.chat.dto.ChatListResponseDto
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

    suspend fun getChatDetail(chatRoomMessageId: String): Result<ChatDetailResponseDto> {
        return runCatching {
            val response = chatRemoteDataSource.getChatDetail(chatRoomMessageId)
            when (response.code()) {
                200 -> response.body() ?: throw Exception("Empty response body")
                403 -> {
                    Log.e(TAG, "Server error 403: ${response.errorBody()?.string()}")
                    throw ForbiddenChatAccessException("Server error")
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