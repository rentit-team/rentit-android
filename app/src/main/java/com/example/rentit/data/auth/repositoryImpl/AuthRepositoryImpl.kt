package com.example.rentit.data.auth.repositoryImpl

import com.example.rentit.core.exceptions.BadRequestException
import com.example.rentit.core.network.safeApiCall
import com.example.rentit.data.auth.dto.GoogleLoginResponseDto
import com.example.rentit.data.auth.local.AuthPrefsDataSource
import com.example.rentit.data.auth.remote.AuthRemoteDataSource
import com.example.rentit.data.auth.dto.SendPhoneCodeResponseDto
import com.example.rentit.data.auth.dto.VerifyPhoneCodeResponseDto
import com.example.rentit.domain.auth.respository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val prefsDataSource: AuthPrefsDataSource,
    private val remoteDataSource: AuthRemoteDataSource
): AuthRepository {
    override suspend fun googleLogin(code: String, redirectUri: String): Result<GoogleLoginResponseDto> {
        return safeApiCall { remoteDataSource.googleLogin(code, redirectUri) }
    }

    override suspend fun sendPhoneCode(phoneNumber: String): Result<SendPhoneCodeResponseDto> {
        return safeApiCall { remoteDataSource.sendPhoneCode(phoneNumber) }
    }

    override suspend fun verifyPhoneCode(phoneNumber: String, code: String): Result<VerifyPhoneCodeResponseDto> {
        return safeApiCall { remoteDataSource.verifyPhoneCode(phoneNumber, code) }
    }

    override suspend fun signUp(name: String, email: String, nickname: String, profileImageUrl: String): Result<Unit> {
        return safeApiCall { remoteDataSource.signUp(name, email, nickname, profileImageUrl) }
    }


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
