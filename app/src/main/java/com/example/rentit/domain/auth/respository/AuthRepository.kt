package com.example.rentit.domain.auth.respository

import com.example.rentit.data.auth.dto.GoogleLoginResponseDto

interface AuthRepository {
    suspend fun googleLogin(code: String, redirectUri: String): Result<GoogleLoginResponseDto>

    suspend fun refreshAccessToken(): Result<String>

    fun getAccessTokenFromPrefs(): String?

    fun saveAccessTokenToPrefs(token: String)

    fun getRefreshTokenFromPrefs(): String?

    fun saveRefreshTokenToPrefs(token: String)

    fun clearPrefs()
}
