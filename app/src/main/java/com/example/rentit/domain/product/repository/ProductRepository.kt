package com.example.rentit.domain.product.repository

import com.example.rentit.data.product.dto.ResvRequestDto
import com.example.rentit.data.product.dto.ResvResponseDto
import com.example.rentit.data.product.dto.CategoryListResponseDto
import com.example.rentit.data.product.dto.CreatePostResponseDto
import com.example.rentit.data.product.dto.ProductDetailResponseDto
import com.example.rentit.data.product.dto.ProductReservedDatesResponseDto
import com.example.rentit.data.product.dto.ProductListResponseDto
import com.example.rentit.data.product.dto.RequestHistoryResponseDto
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface ProductRepository {
    suspend fun getProductList(): Result<ProductListResponseDto>

    suspend fun getProductDetail(productId: Int): Result<ProductDetailResponseDto>

    suspend fun getReservedDates(productId: Int): Result<ProductReservedDatesResponseDto>

    suspend fun postResv(productId: Int, request: ResvRequestDto): Result<ResvResponseDto>

    suspend fun getCategories(): Result<CategoryListResponseDto>

    suspend fun createPost(payLoad: RequestBody, thumbnailImg: MultipartBody.Part?): Result<CreatePostResponseDto>

    suspend fun getProductRequestList(productId: Int): Result<RequestHistoryResponseDto>
}
