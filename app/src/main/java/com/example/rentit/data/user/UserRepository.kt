package com.example.rentit.data.user

import android.util.Log
import com.example.rentit.data.user.dto.LoginResponseDto
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val remoteDataSource: UserRemoteDataSource
) {
    suspend fun googleLogin(code: String, redirectUri: String): Result<Response<LoginResponseDto>> {
        return try {
            val response = remoteDataSource.googleLogin(code, redirectUri)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
