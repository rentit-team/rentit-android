package com.example.rentit.data.user.remote

import com.example.rentit.data.user.dto.GoogleLoginRequestDto
import com.example.rentit.data.user.dto.GoogleLoginResponseDto
import retrofit2.Response
import javax.inject.Inject

class UserRemoteDataSource @Inject constructor(
    private val userApiService: UserApiService
) {
    suspend fun googleLogin(code: String, redirectUri: String): Response<GoogleLoginResponseDto> {
        val request = GoogleLoginRequestDto(code, redirectUri)
        return userApiService.googleLogin(request)
    }
}