package com.example.rentit.data.product.remote

import com.example.rentit.data.product.dto.ProductListResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface ProductApiService {
    @GET("api/v1/products")
    @Headers("Content-Type: application/json")
    suspend fun getProductList(): Response<ProductListResponseDto>
}
