package com.example.rentit.domain.product.model

data class ProductDetailModel(
    val productId: Int,
    val price: Int,
    val title: String,
    val category: List<String>,
    val content: String,
    val createdAt: String,
    val imgUrlList: List<String?>,
)