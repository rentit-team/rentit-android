package com.example.rentit.data.user.remote

import com.example.rentit.data.user.dto.GoogleLoginRequestDto
import com.example.rentit.data.user.dto.GoogleLoginResponseDto
import retrofit2.Response
import javax.inject.Inject

class UserRemoteDataSource @Inject constructor(
    private val userService: UserApiService
) {
    suspend fun googleLogin(code: String, redirectUri: String): Response<GoogleLoginResponseDto> {
        val request = GoogleLoginRequestDto(code, redirectUri)
        return userService.googleLogin(request)
    }
}