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
import javax.inject.Inject

class AuthRemoteDataSource @Inject constructor(
    private val authApiService: AuthApiService
) {
    suspend fun googleLogin(code: String, redirectUri: String): Response<GoogleLoginResponseDto> {
        val request = GoogleLoginRequestDto(code, redirectUri)
        return authApiService.googleLogin(request)
    }

    suspend fun signUp(name: String, email: String, nickname: String, profileImageUrl: String): Response<Unit> {
        val request = SignUpRequestDto(name, email, nickname, profileImageUrl)
        return authApiService.signUp(request)
    }

    suspend fun sendPhoneCode(phoneNumber: String): Response<SendPhoneCodeResponseDto> {
        val request = SendPhoneCodeRequestDto(phoneNumber)
        return authApiService.sendPhoneCode(request)
    }

    suspend fun verifyPhoneCode(phoneNumber: String, code: String): Response<VerifyPhoneCodeResponseDto> {
        val request = VerifyPhoneCodeRequestDto(phoneNumber, code)
        return authApiService.verifyPhoneCode(request)
    }

    suspend fun refreshAccessToken(refreshToken: String): Response<RefreshAccessTokenResponseDto> {
        val request = RefreshAccessTokenRequestDto(refreshToken)
        return authApiService.refreshAccessToken(request)
    }
}