package com.example.rentit.data.user.repositoryImpl

import com.example.rentit.core.network.safeApiCall
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

    override suspend fun getMyInfo(): Result<MyInfoResponseDto> {
        return safeApiCall(remoteDataSource::getMyInfo)
    }

    override suspend fun getMyProductList(): Result<MyProductListResponseDto> {
        return safeApiCall(remoteDataSource::getMyProductList)
    }

    override suspend fun getMyRentalList(): Result<MyRentalListResponseDto> {
        return safeApiCall(remoteDataSource::getMyRentalList)
    }

    override fun getAuthUserIdFromPrefs(): Long = prefsDataSource.getAuthUserIdFromPrefs()

    override fun saveAuthUserIdToPrefs(authUserId: Long) = prefsDataSource.saveAuthUserIdToPrefs(authUserId)

    override fun getTokenFromPrefs(): String? = prefsDataSource.getTokenFromPrefs()

    override fun saveTokenToPrefs(token: String) = prefsDataSource.saveTokenToPrefs(token)

    override fun clearPrefs() = prefsDataSource.clearPrefs()
}
