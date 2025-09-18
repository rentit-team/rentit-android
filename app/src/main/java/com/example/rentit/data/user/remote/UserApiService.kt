package com.example.rentit.data.user.remote

import com.example.rentit.data.user.dto.MyInfoResponseDto
import com.example.rentit.data.user.dto.MyProductListResponseDto
import com.example.rentit.data.user.dto.MyProductsRentalListResponseDto
import com.example.rentit.data.user.dto.MyRentalListResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface UserApiService {
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
