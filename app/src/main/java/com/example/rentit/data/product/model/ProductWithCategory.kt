package com.example.rentit.data.product.model

import com.example.rentit.common.enums.ProductStatus

data class ProductWithCategory(
    val productId: Int,
    val title: String,
    val thumbnailImgUrl: String?,
    val price: Int,
    val minPeriod: Int?,
    val maxPeriod: Int?,
    val status: ProductStatus,
    val parentCategoryIds: List<Int>,
    val categoryNames: List<String>,
    val createdAt: String
)