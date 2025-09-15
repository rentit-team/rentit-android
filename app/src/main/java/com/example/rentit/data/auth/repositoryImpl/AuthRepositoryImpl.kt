package com.example.rentit.data.auth.repositoryImpl

import com.example.rentit.core.error.BadRequestException
import com.example.rentit.core.network.safeApiCall
import com.example.rentit.data.auth.local.AuthPrefsDataSource
import com.example.rentit.data.auth.remote.AuthRemoteDataSource
import com.example.rentit.domain.auth.respository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val prefsDataSource: AuthPrefsDataSource,
    private val remoteDataSource: AuthRemoteDataSource
): AuthRepository {
    override suspend fun refreshAccessToken(): Result<String> {
        val refreshToken = prefsDataSource.getRefreshTokenFromPrefs()
        return if (refreshToken == null) {
            Result.failure(BadRequestException("refresh token is null"))
        } else {
            safeApiCall { remoteDataSource.refreshAccessToken(refreshToken) }
                .map { it.data.accessToken.data }
        }
    }

    override fun getRefreshTokenFromPrefs(): String? = prefsDataSource.getRefreshTokenFromPrefs()

    override fun saveRefreshTokenToPrefs(token: String) = prefsDataSource.saveRefreshTokenToPrefs(token)

    override fun getAccessTokenFromPrefs(): String? = prefsDataSource.getAccessTokenFromPrefs()

    override fun saveAccessTokenToPrefs(token: String) = prefsDataSource.saveAccessTokenToPrefs(token)

    override fun clearPrefs() = prefsDataSource.clearPrefs()
}
