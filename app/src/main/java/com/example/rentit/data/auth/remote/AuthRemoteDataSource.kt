package com.example.rentit.data.auth.remote

import com.example.rentit.data.auth.dto.GoogleLoginRequestDto
import com.example.rentit.data.auth.dto.GoogleLoginResponseDto
import com.example.rentit.data.auth.dto.RefreshAccessTokenRequestDto
import com.example.rentit.data.auth.dto.RefreshAccessTokenResponseDto
import retrofit2.Response
import javax.inject.Inject

class AuthRemoteDataSource @Inject constructor(
    private val authApiService: AuthApiService
) {
    suspend fun googleLogin(code: String, redirectUri: String): Response<GoogleLoginResponseDto> {
        val request = GoogleLoginRequestDto(code, redirectUri)
        return authApiService.googleLogin(request)
    }

    suspend fun refreshAccessToken(refreshToken: String): Response<RefreshAccessTokenResponseDto> {
        val request = RefreshAccessTokenRequestDto(refreshToken)
        return authApiService.refreshAccessToken(request)
    }
}