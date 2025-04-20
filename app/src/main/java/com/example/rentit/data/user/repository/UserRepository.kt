package com.example.rentit.data.user.repository

import com.example.rentit.data.user.dto.LoginResponseDto
import com.example.rentit.data.user.remote.UserRemoteDataSource
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val remoteDataSource: UserRemoteDataSource
) {
    suspend fun googleLogin(code: String, redirectUri: String): Result<LoginResponseDto> {
        return try {
            val response = remoteDataSource.googleLogin(code, redirectUri)
            when(response.code()) {
                200 -> {
                    val body = response.body()
                    if(body != null) {
                        Result.success(body)
                    } else {
                        Result.failure(Exception("Empty response body"))
                    }
                }
                409 -> {
                    val errorMsg = response.errorBody()?.string() ?: "Client error"
                    Result.failure(Exception("Client error: $errorMsg"))
                }
                500 -> {
                    Result.failure(Exception("Server error"))
                }
                else -> {
                    Result.failure(Exception("Unexpected error"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
