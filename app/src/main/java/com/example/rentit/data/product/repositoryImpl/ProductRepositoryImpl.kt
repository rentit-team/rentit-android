package com.example.rentit.data.product.repositoryImpl

import com.example.rentit.core.network.safeApiCall
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
        return safeApiCall(productRemoteDataSource::getProductList)
    }

    override suspend fun getProductDetail(productId: Int): Result<ProductDetailResponseDto> {
        return safeApiCall { productRemoteDataSource.getProductDetail(productId) }
    }

    override suspend fun getReservedDates(productId: Int): Result<ProductReservedDatesResponseDto> {
        return safeApiCall { productRemoteDataSource.getReservedDates(productId) }
    }

    override suspend fun postResv(productId: Int, request: ResvRequestDto): Result<ResvResponseDto> {
        return safeApiCall { productRemoteDataSource.postResv(productId, request) }
    }

    override suspend fun getCategories(): Result<CategoryListResponseDto> {
        return safeApiCall(productRemoteDataSource::getCategories)
    }

    override suspend fun createPost(payLoad: RequestBody, thumbnailImg: MultipartBody.Part?): Result<CreatePostResponseDto> {
        return safeApiCall { productRemoteDataSource.createPost(payLoad, thumbnailImg) }
    }

    override suspend fun getProductRequestList(productId: Int): Result<RequestHistoryResponseDto> {
        return safeApiCall { productRemoteDataSource.getProductRequestList(productId) }
    }
}
