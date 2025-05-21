package com.example.rentit.data.product.remote

import com.example.rentit.data.product.dto.ProductListResponseDto
import retrofit2.Response
import javax.inject.Inject

class ProductRemoteDataSource @Inject constructor(
    private val productApiService: ProductApiService
) {
    suspend fun getProductList(): Response<ProductListResponseDto> {
        return productApiService.getProductList()
    }

}