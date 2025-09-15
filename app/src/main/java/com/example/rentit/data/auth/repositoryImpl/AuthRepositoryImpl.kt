package com.example.rentit.data.auth.repositoryImpl

import com.example.rentit.core.error.BadRequestException
import com.example.rentit.core.network.safeApiCall
import com.example.rentit.data.auth.remote.AuthRemoteDataSource
import com.example.rentit.data.user.local.UserPrefsDataSource
import com.example.rentit.domain.auth.respository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val prefsDataSource: UserPrefsDataSource,
    private val authRemoteDataSource: AuthRemoteDataSource
): AuthRepository {
    override suspend fun refreshAccessToken(): Result<String> {
        val refreshToken = prefsDataSource.getRefreshTokenFromPrefs()
        return if (refreshToken == null) {
            Result.failure(BadRequestException("refresh token is null"))
        } else {
            safeApiCall { authRemoteDataSource.refreshAccessToken(refreshToken) }
                .map { it.data.accessToken.data }
        }
    }

    override fun getAccessTokenFromPrefs(): String? = prefsDataSource.getAccessTokenFromPrefs()

    override fun clearPrefs() = prefsDataSource.clearPrefs()
}
