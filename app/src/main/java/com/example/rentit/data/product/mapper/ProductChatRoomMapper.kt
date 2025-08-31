package com.example.rentit.data.product.mapper

import com.example.rentit.data.product.dto.ProductDto
import com.example.rentit.domain.product.model.ProductChatRoomSummaryModel

fun ProductDto.toChatRoomSummaryModel() =
    ProductChatRoomSummaryModel(
        thumbnailImgUrl = thumbnailImgUrl,
        status = status,
        title = title,
        price = price,
        minPeriod = period?.min,
        maxPeriod = period?.max
    )