package com.example.rentit.data.product.remote

import com.example.rentit.data.product.dto.BookingRequestDto
import com.example.rentit.data.product.dto.BookingResponseDto
import com.example.rentit.data.product.dto.CategoryListResponseDto
import com.example.rentit.data.product.dto.CreatePostResponseDto
import com.example.rentit.data.product.dto.ProductDetailResponseDto
import com.example.rentit.data.product.dto.ProductReservedDatesResponseDto
import com.example.rentit.data.product.dto.ProductListResponseDto
import com.example.rentit.data.product.dto.RequestHistoryResponseDto
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
    suspend fun postBooking(productId: Int, request: BookingRequestDto): Response<BookingResponseDto> {
        return productApiService.postBooking(productId, request)
    }
    suspend fun getCategories(): Response<CategoryListResponseDto> {
        return productApiService.getCategories()
    }
    suspend fun createPost(payLoad: RequestBody, thumbnailImg: MultipartBody.Part?): Response<CreatePostResponseDto> {
        return productApiService.createPost(payLoad, thumbnailImg)
    }
    suspend fun getProductRequestList(productId: Int): Response<RequestHistoryResponseDto> {
        return productApiService.getProductRequestList(productId)
    }

}