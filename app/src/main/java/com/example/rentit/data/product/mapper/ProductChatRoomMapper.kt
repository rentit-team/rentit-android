package com.example.rentit.data.product.mapper

import com.example.rentit.data.product.dto.ProductDto
import com.example.rentit.domain.chat.model.ChatRoomProductSummaryModel

fun ProductDto.toChatRoomSummaryModel() =
    ChatRoomProductSummaryModel(
        productId = productId,
        thumbnailImgUrl = thumbnailImgUrl,
        status = status,
        title = title,
        price = price,
        minPeriod = period?.min,
        maxPeriod = period?.max
    )