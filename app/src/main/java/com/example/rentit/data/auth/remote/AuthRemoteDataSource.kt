package com.example.rentit.data.auth.remote

import com.example.rentit.data.user.dto.RefreshAccessTokenRequestDto
import com.example.rentit.data.user.dto.RefreshAccessTokenResponseDto
import retrofit2.Response
import javax.inject.Inject

class AuthRemoteDataSource @Inject constructor(
    private val authApiService: AuthApiService
) {
    suspend fun refreshAccessToken(refreshToken: String): Response<RefreshAccessTokenResponseDto> {
        val request = RefreshAccessTokenRequestDto(refreshToken)
        return authApiService.refreshAccessToken(request)
    }
}