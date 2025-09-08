package com.example.rentit.data.user.repositoryImpl

import com.example.rentit.core.network.getOrThrow
import com.example.rentit.data.user.dto.GoogleLoginResponseDto
import com.example.rentit.data.user.dto.MyInfoResponseDto
import com.example.rentit.data.user.dto.MyProductListResponseDto
import com.example.rentit.data.user.dto.MyRentalListResponseDto
import com.example.rentit.data.user.dto.SendPhoneCodeResponseDto
import com.example.rentit.data.user.dto.VerifyPhoneCodeResponseDto
import com.example.rentit.data.user.local.UserPrefsDataSource
import com.example.rentit.data.user.remote.UserRemoteDataSource
import com.example.rentit.domain.user.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val prefsDataSource: UserPrefsDataSource,
    private val remoteDataSource: UserRemoteDataSource
): UserRepository {
    override suspend fun googleLogin(code: String, redirectUri: String): Result<GoogleLoginResponseDto> {
        val response = remoteDataSource.googleLogin(code, redirectUri)
        return response.getOrThrow()
    }

    override suspend fun sendPhoneCode(phoneNumber: String): Result<SendPhoneCodeResponseDto> {
        val response = remoteDataSource.sendPhoneCode(phoneNumber)
        return response.getOrThrow()
    }

    override suspend fun verifyPhoneCode(phoneNumber: String, code: String): Result<VerifyPhoneCodeResponseDto> {
        val response = remoteDataSource.verifyPhoneCode(phoneNumber, code)
        return response.getOrThrow()
    }

    override suspend fun signUp(name: String, email: String, nickname: String, profileImageUrl: String): Result<Unit> {
        val response = remoteDataSource.signUp(name, email, nickname, profileImageUrl)
        return response.getOrThrow()
    }

    override suspend fun getMyInfo(): Result<MyInfoResponseDto> {
        val response = remoteDataSource.getMyInfo()
        return response.getOrThrow()
    }

    override suspend fun getMyProductList(): Result<MyProductListResponseDto> {
        val response = remoteDataSource.getMyProductList()
        return response.getOrThrow()
    }

    override suspend fun getMyRentalList(): Result<MyRentalListResponseDto> {
        val response = remoteDataSource.getMyRentalList()
        return response.getOrThrow()
    }

    override fun getAuthUserIdFromPrefs(): Long = prefsDataSource.getAuthUserIdFromPrefs()

    override fun saveAuthUserIdToPrefs(authUserId: Long) = prefsDataSource.saveAuthUserIdToPrefs(authUserId)

    override fun getTokenFromPrefs(): String? = prefsDataSource.getTokenFromPrefs()

    override fun saveTokenToPrefs(token: String) = prefsDataSource.saveTokenToPrefs(token)

    override fun clearPrefs() = prefsDataSource.clearPrefs()
}
