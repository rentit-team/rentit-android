package com.example.rentit.data.user.remote

import com.example.rentit.data.user.dto.GoogleLoginRequestDto
import com.example.rentit.data.user.dto.GoogleLoginResponseDto
import com.example.rentit.data.user.dto.MyInfoResponseDto
import com.example.rentit.data.user.dto.MyProductListResponseDto
import com.example.rentit.data.user.dto.MyProductsRentalListResponseDto
import com.example.rentit.data.user.dto.MyRentalListResponseDto
import com.example.rentit.data.user.dto.SendPhoneCodeRequestDto
import com.example.rentit.data.user.dto.SendPhoneCodeResponseDto
import com.example.rentit.data.user.dto.SignUpRequestDto
import com.example.rentit.data.user.dto.VerifyPhoneCodeRequestDto
import com.example.rentit.data.user.dto.VerifyPhoneCodeResponseDto
import retrofit2.Response
import javax.inject.Inject

class UserRemoteDataSource @Inject constructor(
    private val userApiService: UserApiService
) {
    suspend fun googleLogin(code: String, redirectUri: String): Response<GoogleLoginResponseDto> {
        val request = GoogleLoginRequestDto(code, redirectUri)
        return userApiService.googleLogin(request)
    }

    suspend fun signUp(name: String, email: String, nickname: String, profileImageUrl: String): Response<Unit> {
        val request = SignUpRequestDto(name, email, nickname, profileImageUrl)
        return userApiService.signUp(request)
    }

    suspend fun sendPhoneCode(phoneNumber: String): Response<SendPhoneCodeResponseDto> {
        val request = SendPhoneCodeRequestDto(phoneNumber)
        return userApiService.sendPhoneCode(request)
    }

    suspend fun verifyPhoneCode(phoneNumber: String, code: String): Response<VerifyPhoneCodeResponseDto> {
        val request = VerifyPhoneCodeRequestDto(phoneNumber, code)
        return userApiService.verifyPhoneCode(request)
    }

    suspend fun getMyInfo(): Response<MyInfoResponseDto> {
        return userApiService.getMyInfo()
    }

    suspend fun getMyProductList(): Response<MyProductListResponseDto> {
        return userApiService.getMyProductList()
    }

    suspend fun getMyRentalList(): Response<MyRentalListResponseDto> {
        return userApiService.getMyRentalList()
    }

    suspend fun getMyProductsRentalList(): Response<MyProductsRentalListResponseDto> {
        return userApiService.getMyProductsRentalList()
    }
}