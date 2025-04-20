package com.example.rentit.data.user.remote

import com.example.rentit.data.user.dto.LoginRequestDto
import com.example.rentit.data.user.dto.LoginResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface UserApiService {
    @POST("api/v1/auth/google")
    @Headers("Content-Type: application/json")
    suspend fun googleLogin(@Body request: LoginRequestDto): Response<LoginResponseDto>
}
