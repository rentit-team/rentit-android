package com.example.rentit.data.product.repository

import com.example.rentit.data.product.dto.ProductListResponseDto
import com.example.rentit.data.product.remote.ProductRemoteDataSource
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val productRemoteDataSource: ProductRemoteDataSource
) {
    suspend fun getProductList(): Result<ProductListResponseDto> {
        return try {
            val response = productRemoteDataSource.getProductList()
            when(response.code()) {
                200 -> {
                    val body = response.body()
                    if(body != null) {
                        Result.success(body)
                    } else {
                        Result.failure(Exception("Empty response body"))
                    }
                }
                500 -> {
                    Result.failure(Exception("Server error"))
                }
                else -> {
                    Result.failure(Exception("Unexpected error"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
