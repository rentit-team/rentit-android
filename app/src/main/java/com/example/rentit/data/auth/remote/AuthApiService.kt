package com.example.rentit.data.auth.remote

import com.example.rentit.data.auth.dto.GoogleLoginRequestDto
import com.example.rentit.data.auth.dto.GoogleLoginResponseDto
import com.example.rentit.data.auth.dto.RefreshAccessTokenRequestDto
import com.example.rentit.data.auth.dto.RefreshAccessTokenResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthApiService {
    @POST("api/v1/auth/google")
    @Headers("Content-Type: application/json")
    suspend fun googleLogin(@Body request: GoogleLoginRequestDto): Response<GoogleLoginResponseDto>

    @POST("api/v1/auth/refresh")
    @Headers("Content-Type: application/json")
    suspend fun refreshAccessToken(@Body request: RefreshAccessTokenRequestDto): Response<RefreshAccessTokenResponseDto>
}
