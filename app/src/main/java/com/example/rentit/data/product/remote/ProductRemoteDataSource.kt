package com.example.rentit.data.product.remote

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
import retrofit2.Response
import javax.inject.Inject

class ProductRemoteDataSource @Inject constructor(
    private val productApiService: ProductApiService
) {
    suspend fun getProductList(): Response<ProductListResponseDto> {
        return productApiService.getProductList()
    }
    suspend fun getProductDetail(productId: Int): Response<ProductDetailResponseDto> {
        return productApiService.getProductDetail(productId)
    }
    suspend fun getReservedDates(productId: Int): Response<ProductReservedDatesResponseDto> {
        return productApiService.getReservedDates(productId)
    }
    suspend fun postReservation(productId: Int, request: ReservationRequestDto): Response<ReservationResponseDto> {
        return productApiService.postReservation(productId, request)
    }
    suspend fun getCategories(): Response<CategoryListResponseDto> {
        return productApiService.getCategories()
    }
    suspend fun createPost(payLoad: RequestBody, thumbnailImg: MultipartBody.Part?): Response<CreatePostResponseDto> {
        return productApiService.createPost(payLoad, thumbnailImg)
    }
    suspend fun getChatAccessibility(productId: Int): Response<ChatAccessibilityResponseDto> {
        return productApiService.getChatAccessibility(productId)
    }
}