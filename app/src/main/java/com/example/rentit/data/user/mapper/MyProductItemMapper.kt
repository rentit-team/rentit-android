package com.example.rentit.data.user.mapper

import com.example.rentit.data.product.dto.ProductDto
import com.example.rentit.domain.product.model.CategoryModel
import com.example.rentit.domain.user.model.MyProductItemModel

fun ProductDto.toMyProductItemModel(categoryMap: Map<Int, CategoryModel>) =
    MyProductItemModel(
        productId = productId,
        title = title,
        price = price,
        thumbnailImgUrl = thumbnailImgUrl,
        minPeriod = period?.min,
        maxPeriod = period?.max,
        categories = categories.mapNotNull { categoryMap[it]?.name },
        status = status,
        createdAt = createdAt,
    )