package com.example.rentit.domain.auth.respository

import com.example.rentit.data.auth.dto.GoogleLoginResponseDto
import com.example.rentit.data.auth.dto.SendPhoneCodeResponseDto
import com.example.rentit.data.auth.dto.VerifyPhoneCodeResponseDto

interface AuthRepository {
    suspend fun googleLogin(code: String, redirectUri: String): Result<GoogleLoginResponseDto>

    suspend fun refreshAccessToken(): Result<String>

    suspend fun sendPhoneCode(phoneNumber: String): Result<SendPhoneCodeResponseDto>

    suspend fun verifyPhoneCode(phoneNumber: String, code: String): Result<VerifyPhoneCodeResponseDto>

    suspend fun signUp(name: String, email: String, nickname: String, profileImageUrl: String): Result<Unit>

    fun getAccessTokenFromPrefs(): String?

    fun saveAccessTokenToPrefs(token: String)

    fun getRefreshTokenFromPrefs(): String?

    fun saveRefreshTokenToPrefs(token: String)

    fun clearPrefs()
}
