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
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST

interface UserApiService {
    @POST("api/v1/auth/google")
    @Headers("Content-Type: application/json")
    suspend fun googleLogin(@Body request: GoogleLoginRequestDto): Response<GoogleLoginResponseDto>

    @POST("api/v1/users/phone-verification/request")
    @Headers("Content-Type: application/json")
    suspend fun sendPhoneCode(@Body request: SendPhoneCodeRequestDto): Response<SendPhoneCodeResponseDto>

    @PATCH("api/v1/users/phone-verification/verify")
    @Headers("Content-Type: application/json")
    suspend fun verifyPhoneCode(@Body request: VerifyPhoneCodeRequestDto): Response<VerifyPhoneCodeResponseDto>

    @POST("api/v1/users/signup")
    @Headers("Content-Type: application/json")
    suspend fun signUp(@Body request: SignUpRequestDto): Response<Unit>

    @GET("api/v1/users/me")
    @Headers("Content-Type: application/json")
    suspend fun getMyInfo(): Response<MyInfoResponseDto>

    @GET("api/v1/users/products")
    @Headers("Content-Type: application/json")
    suspend fun getMyProductList(): Response<MyProductListResponseDto>

    @GET("api/v1/users/reservations")
    @Headers("Content-Type: application/json")
    suspend fun getMyRentalList(): Response<MyRentalListResponseDto>

    @GET("api/v1/users/rental-history")
    @Headers("Content-Type: application/json")
    suspend fun getMyProductsRentalList(): Response<MyProductsRentalListResponseDto>
}
