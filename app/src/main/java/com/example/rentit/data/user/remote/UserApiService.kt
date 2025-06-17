package com.example.rentit.data.user.remote

import com.example.rentit.data.user.dto.GoogleLoginRequestDto
import com.example.rentit.data.user.dto.GoogleLoginResponseDto
import com.example.rentit.data.user.dto.MyProductListResponseDto
import com.example.rentit.data.user.dto.SignUpRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface UserApiService {
    @POST("api/v1/auth/google")
    @Headers("Content-Type: application/json")
    suspend fun googleLogin(@Body request: GoogleLoginRequestDto): Response<GoogleLoginResponseDto>

    @POST("api/v1/users/signup")
    @Headers("Content-Type: application/json")
    suspend fun signUp(@Body request: SignUpRequestDto): Response<Unit>

    @GET("api/v1/users/products")
    @Headers("Content-Type: application/json")
    suspend fun getMyProductList(): Response<MyProductListResponseDto>
}
