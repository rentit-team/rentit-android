package com.example.rentit.domain.auth.respository

interface AuthRepository {
    suspend fun refreshAccessToken(): Result<String>

    fun getAccessTokenFromPrefs(): String?

    fun clearPrefs()
}
