package com.example.rentit.data.auth.remote

import com.example.rentit.data.auth.dto.GoogleLoginRequestDto
import com.example.rentit.data.auth.dto.GoogleLoginResponseDto
import com.example.rentit.data.auth.dto.RefreshAccessTokenRequestDto
import com.example.rentit.data.auth.dto.RefreshAccessTokenResponseDto
import com.example.rentit.data.auth.dto.SendPhoneCodeRequestDto
import com.example.rentit.data.auth.dto.SendPhoneCodeResponseDto
import com.example.rentit.data.auth.dto.SignUpRequestDto
import com.example.rentit.data.auth.dto.VerifyPhoneCodeRequestDto
import com.example.rentit.data.auth.dto.VerifyPhoneCodeResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST

interface AuthApiService {
    @POST("api/v1/auth/google")
    @Headers("Content-Type: application/json")
    suspend fun googleLogin(@Body request: GoogleLoginRequestDto): Response<GoogleLoginResponseDto>

    @POST("api/v1/users/phone-verification/request")
    @Headers("Content-Type: application/json")
    suspend fun sendPhoneCode(@Body request: SendPhoneCodeRequestDto): Response<SendPhoneCodeResponseDto>

    @PATCH("api/v1/users/phone-verification/verify")
    @Headers("Content-Type: application/json")
    suspend fun verifyPhoneCode(@Body request: VerifyPhoneCodeRequestDto): Response<VerifyPhoneCodeResponseDto>

    @POST("api/v1/users/signup")
    @Headers("Content-Type: application/json")
    suspend fun signUp(@Body request: SignUpRequestDto): Response<Unit>

    @POST("api/v1/auth/refresh")
    @Headers("Content-Type: application/json")
    suspend fun refreshAccessToken(@Body request: RefreshAccessTokenRequestDto): Response<RefreshAccessTokenResponseDto>
}
