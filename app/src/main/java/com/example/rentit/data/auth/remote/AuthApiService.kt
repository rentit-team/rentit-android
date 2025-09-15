package com.example.rentit.data.auth.remote

import com.example.rentit.data.user.dto.RefreshAccessTokenRequestDto
import com.example.rentit.data.user.dto.RefreshAccessTokenResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthApiService {
    @POST("api/v1/auth/refresh")
    @Headers("Content-Type: application/json")
    suspend fun refreshAccessToken(@Body request: RefreshAccessTokenRequestDto): Response<RefreshAccessTokenResponseDto>
}
