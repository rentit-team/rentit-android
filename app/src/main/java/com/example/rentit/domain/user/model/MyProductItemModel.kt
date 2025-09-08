package com.example.rentit.domain.user.model

import com.example.rentit.common.enums.ProductStatus

data class MyProductItemModel(
    val productId: Int,
    val title: String,
    val price: Int,
    val thumbnailImgUrl: String?,
    val minPeriod: Int?,
    val maxPeriod: Int?,
    val categories: List<String>,
    val status: ProductStatus,
    val createdAt: String,
)