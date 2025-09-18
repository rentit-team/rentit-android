package com.example.rentit.domain.user.repository

import com.example.rentit.data.user.dto.MyInfoResponseDto
import com.example.rentit.data.user.dto.MyProductListResponseDto
import com.example.rentit.data.user.dto.MyProductsRentalListResponseDto
import com.example.rentit.data.user.dto.MyRentalListResponseDto

interface UserRepository {
    suspend fun getMyInfo(): Result<MyInfoResponseDto>

    suspend fun getMyProductList(): Result<MyProductListResponseDto>

    suspend fun getMyRentalList(): Result<MyRentalListResponseDto>

    suspend fun getMyProductsRentalList(): Result<MyProductsRentalListResponseDto>

    fun getAuthUserIdFromPrefs(): Long

    fun saveAuthUserIdToPrefs(authUserId: Long)

    fun getAuthNicknameFromPrefs(): String

    fun saveAuthNicknameToPrefs(nickname: String)

    fun clearPrefs()
}
