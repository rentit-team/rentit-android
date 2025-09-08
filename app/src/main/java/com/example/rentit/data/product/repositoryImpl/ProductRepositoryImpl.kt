package com.example.rentit.data.product.repositoryImpl

import com.example.rentit.core.network.getOrThrow
import com.example.rentit.data.product.dto.ResvRequestDto
import com.example.rentit.data.product.dto.ResvResponseDto
import com.example.rentit.data.product.dto.CategoryListResponseDto
import com.example.rentit.data.product.dto.CreatePostResponseDto
import com.example.rentit.data.product.dto.ProductDetailResponseDto
import com.example.rentit.data.product.dto.ProductReservedDatesResponseDto
import com.example.rentit.data.product.dto.ProductListResponseDto
import com.example.rentit.data.product.dto.RequestHistoryResponseDto
import com.example.rentit.data.product.remote.ProductRemoteDataSource
import com.example.rentit.domain.product.repository.ProductRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val productRemoteDataSource: ProductRemoteDataSource
): ProductRepository {
    override suspend fun getProductList(): Result<ProductListResponseDto> {
        val response = productRemoteDataSource.getProductList()
        return response.getOrThrow()
    }

    override suspend fun getProductDetail(productId: Int): Result<ProductDetailResponseDto> {
        val response = productRemoteDataSource.getProductDetail(productId)
        return response.getOrThrow()
    }

    override suspend fun getReservedDates(productId: Int): Result<ProductReservedDatesResponseDto> {
        val response = productRemoteDataSource.getReservedDates(productId)
        return response.getOrThrow()
    }

    override suspend fun postResv(productId: Int, request: ResvRequestDto): Result<ResvResponseDto> {
        val response = productRemoteDataSource.postResv(productId, request)
        return response.getOrThrow()
    }

    override suspend fun getCategories(): Result<CategoryListResponseDto> {
        val response = productRemoteDataSource.getCategories()
        return response.getOrThrow()
    }

    override suspend fun createPost(payLoad: RequestBody, thumbnailImg: MultipartBody.Part?): Result<CreatePostResponseDto> {
        val response = productRemoteDataSource.createPost(payLoad, thumbnailImg)
        return response.getOrThrow()
    }

    override suspend fun getProductRequestList(productId: Int): Result<RequestHistoryResponseDto> {
        val response = productRemoteDataSource.getProductRequestList(productId)
        return response.getOrThrow()
    }
}
