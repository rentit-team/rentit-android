package com.example.rentit.domain.product.model

data class ProductDetailResultModel(
    val productDetail: ProductDetailModel,
    val requestCount: Int? = null
)