package com.example.rentit.domain.product.model

import com.example.rentit.common.enums.ProductStatus

data class ProductChatRoomSummaryModel (
    val productId: Int,
    val thumbnailImgUrl: String?,
    val title: String,
    val status: ProductStatus,
    val price: Int,
    val minPeriod: Int?,
    val maxPeriod: Int?,
)