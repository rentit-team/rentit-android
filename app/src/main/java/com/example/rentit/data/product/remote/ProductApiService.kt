package com.example.rentit.data.product.remote

import com.example.rentit.data.product.dto.BookingRequestDto
import com.example.rentit.data.product.dto.BookingResponseDto
import com.example.rentit.data.product.dto.ProductDetailResponseDto
import com.example.rentit.data.product.dto.ProductReservedDatesResponseDto
import com.example.rentit.data.product.dto.ProductListResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface ProductApiService {
    @GET("api/v1/products")
    @Headers("Content-Type: application/json")
    suspend fun getProductList(): Response<ProductListResponseDto>

    @GET("api/v1/products/{productId}")
    @Headers("Content-Type: application/json")
    suspend fun getProductDetail(@Path("productId") productId: Int): Response<ProductDetailResponseDto>

    @GET("api/v1/products/{productId}/reservations/dates")
    @Headers("Content-Type: application/json")
    suspend fun getReservedDates(@Path("productId") productId: Int): Response<ProductReservedDatesResponseDto>

    @POST("api/v1/products/{productId}/reservations")
    @Headers("Content-Type: application/json")
    suspend fun postBooking(@Path("productId") productId: Int, @Body request: BookingRequestDto): Response<BookingResponseDto>
}
