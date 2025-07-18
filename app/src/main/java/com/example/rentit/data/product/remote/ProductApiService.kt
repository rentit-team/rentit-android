package com.example.rentit.data.product.remote

import com.example.rentit.data.product.dto.ResvRequestDto
import com.example.rentit.data.product.dto.ResvResponseDto
import com.example.rentit.data.product.dto.CategoryListResponseDto
import com.example.rentit.data.product.dto.CreatePostResponseDto
import com.example.rentit.data.product.dto.ProductDetailResponseDto
import com.example.rentit.data.product.dto.ProductReservedDatesResponseDto
import com.example.rentit.data.product.dto.ProductListResponseDto
import com.example.rentit.data.product.dto.RequestHistoryResponseDto
import com.example.rentit.data.product.dto.UpdateResvStatusRequestDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
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
    suspend fun postResv(@Path("productId") productId: Int, @Body request: ResvRequestDto): Response<ResvResponseDto>

    @GET("api/v1/products/categories")
    @Headers("Content-Type: application/json")
    suspend fun getCategories(): Response<CategoryListResponseDto>

    @Multipart
    @POST("api/v1/products")
    suspend fun createPost(
        @Part("payload") payload: RequestBody,
        @Part thumbnailImg: MultipartBody.Part?
    ): Response<CreatePostResponseDto>

    @GET("api/v1/products/{productId}/reservations")
    @Headers("Content-Type: application/json")
    suspend fun getProductRequestList(@Path("productId") productId: Int): Response<RequestHistoryResponseDto>

    @PATCH("api/v1/products/{productId}/reservations/{reservationId}")
    @Headers("Content-Type: application/json")
    suspend fun updateResvStatus(
        @Path("productId") productId: Int,
        @Path("reservationId") reservationId: Int,
        @Body request: UpdateResvStatusRequestDto
    ): Response<Unit>
}
