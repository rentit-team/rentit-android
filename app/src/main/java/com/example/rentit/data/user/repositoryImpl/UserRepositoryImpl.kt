package com.example.rentit.data.user.repositoryImpl

import com.example.rentit.core.network.safeApiCall
import com.example.rentit.data.user.dto.MyInfoResponseDto
import com.example.rentit.data.user.dto.MyProductListResponseDto
import com.example.rentit.data.user.dto.MyProductsRentalListResponseDto
import com.example.rentit.data.user.dto.MyRentalListResponseDto
import com.example.rentit.data.user.local.UserPrefsDataSource
import com.example.rentit.data.user.remote.UserRemoteDataSource
import com.example.rentit.domain.user.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val prefsDataSource: UserPrefsDataSource,
    private val remoteDataSource: UserRemoteDataSource
): UserRepository {
    override suspend fun getMyInfo(): Result<MyInfoResponseDto> {
        return safeApiCall(remoteDataSource::getMyInfo)
    }

    override suspend fun getMyProductList(): Result<MyProductListResponseDto> {
        return safeApiCall(remoteDataSource::getMyProductList)
    }

    override suspend fun getMyRentalList(): Result<MyRentalListResponseDto> {
        return safeApiCall(remoteDataSource::getMyRentalList)
    }

    override suspend fun getMyProductsRentalList(): Result<MyProductsRentalListResponseDto> {
        return safeApiCall(remoteDataSource::getMyProductsRentalList)
    }

    override fun getAuthUserIdFromPrefs(): Long = prefsDataSource.getAuthUserIdFromPrefs()

    override fun saveAuthUserIdToPrefs(authUserId: Long) = prefsDataSource.saveAuthUserIdToPrefs(authUserId)

    override fun getAuthNicknameFromPrefs(): String = prefsDataSource.getAuthNicknameFromPrefs()

    override fun saveAuthNicknameToPrefs(nickname: String) = prefsDataSource.saveAuthNicknameToPrefs(nickname)

    override fun clearPrefs() = prefsDataSource.clearPrefs()
}
