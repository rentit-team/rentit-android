package com.example.rentit.domain.user.repository

import com.example.rentit.data.user.dto.GoogleLoginResponseDto
import com.example.rentit.data.user.dto.MyInfoResponseDto
import com.example.rentit.data.user.dto.MyProductListResponseDto
import com.example.rentit.data.user.dto.MyRentalListResponseDto
import com.example.rentit.data.user.dto.SendPhoneCodeResponseDto

interface UserRepository {
    suspend fun googleLogin(code: String, redirectUri: String): Result<GoogleLoginResponseDto>

    suspend fun sendPhoneCode(phoneNumber: String): Result<SendPhoneCodeResponseDto>

    suspend fun verifyPhoneCode(phoneNumber: String, code: String): Result<Unit>

    suspend fun signUp(name: String, email: String, nickname: String, profileImageUrl: String): Result<Unit>

    suspend fun getMyInfo(): Result<MyInfoResponseDto>

    suspend fun getMyProductList(): Result<MyProductListResponseDto>

    suspend fun getMyRentalList(): Result<MyRentalListResponseDto>

    fun getAuthUserIdFromPrefs(): Long

    fun saveAuthUserIdToPrefs(authUserId: Long)

    fun getTokenFromPrefs(): String?

    fun saveTokenToPrefs(token: String)

    fun clearPrefs()
}
