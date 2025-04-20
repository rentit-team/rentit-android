package com.example.rentit.data.user.remote

import com.example.rentit.data.user.dto.LoginRequestDto
import com.example.rentit.data.user.dto.LoginResponseDto
import retrofit2.Response
import javax.inject.Inject

class UserRemoteDataSource @Inject constructor(
    private val userService: UserApiService
) {
    suspend fun googleLogin(code: String, redirectUri: String): Response<LoginResponseDto> {
        val request = LoginRequestDto(code, redirectUri)
        return userService.googleLogin(request)
    }
}