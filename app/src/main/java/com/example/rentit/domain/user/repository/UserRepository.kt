package com.example.rentit.domain.user.repository

import com.example.rentit.data.user.dto.GoogleLoginResponseDto
import com.example.rentit.data.user.dto.MyInfoResponseDto
import com.example.rentit.data.user.dto.MyProductListResponseDto
import com.example.rentit.data.user.dto.MyProductsRentalListResponseDto
import com.example.rentit.data.user.dto.MyRentalListResponseDto
import com.example.rentit.data.user.dto.SendPhoneCodeResponseDto
import com.example.rentit.data.user.dto.VerifyPhoneCodeResponseDto

interface UserRepository {
    suspend fun googleLogin(code: String, redirectUri: String): Result<GoogleLoginResponseDto>

    suspend fun sendPhoneCode(phoneNumber: String): Result<SendPhoneCodeResponseDto>

    suspend fun verifyPhoneCode(phoneNumber: String, code: String): Result<VerifyPhoneCodeResponseDto>

    suspend fun signUp(name: String, email: String, nickname: String, profileImageUrl: String): Result<Unit>

    suspend fun getMyInfo(): Result<MyInfoResponseDto>

    suspend fun getMyProductList(): Result<MyProductListResponseDto>

    suspend fun getMyRentalList(): Result<MyRentalListResponseDto>

    suspend fun getMyProductsRentalList(): Result<MyProductsRentalListResponseDto>

    fun getRefreshTokenFromPrefs(): String?

    fun getAccessTokenFromPrefs(): String?

    fun saveRefreshTokenToPrefs(token: String)

    fun saveAccessTokenToPrefs(token: String)

    fun getAuthUserIdFromPrefs(): Long

    fun saveAuthUserIdToPrefs(authUserId: Long)

    fun getAuthNicknameFromPrefs(): String

    fun saveAuthNicknameToPrefs(nickname: String)

    fun clearPrefs()
}
