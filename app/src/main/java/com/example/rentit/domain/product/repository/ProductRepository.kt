package com.example.rentit.domain.product.repository

import com.example.rentit.data.product.dto.ReservationRequestDto
import com.example.rentit.data.product.dto.ReservationResponseDto
import com.example.rentit.data.product.dto.CategoryListResponseDto
import com.example.rentit.data.product.dto.ChatAccessibilityResponseDto
import com.example.rentit.data.product.dto.CreatePostResponseDto
import com.example.rentit.data.product.dto.ProductDetailResponseDto
import com.example.rentit.data.product.dto.ProductReservedDatesResponseDto
import com.example.rentit.data.product.dto.ProductListResponseDto
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface ProductRepository {
    suspend fun getProductList(): Result<ProductListResponseDto>

    suspend fun getProductDetail(productId: Int): Result<ProductDetailResponseDto>

    suspend fun getReservedDates(productId: Int): Result<ProductReservedDatesResponseDto>

    suspend fun postReservation(productId: Int, request: ReservationRequestDto): Result<ReservationResponseDto>

    suspend fun getCategories(): Result<CategoryListResponseDto>

    suspend fun createPost(payLoad: RequestBody, thumbnailImg: MultipartBody.Part?): Result<CreatePostResponseDto>

    suspend fun getChatAccessibility(productId: Int): Result<ChatAccessibilityResponseDto>
}
