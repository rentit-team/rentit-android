package com.example.rentit.data.product.mapper

import com.example.rentit.data.product.dto.ProductDto
import com.example.rentit.domain.product.model.CategoryModel
import com.example.rentit.domain.product.model.ProductDetailModel

fun ProductDto.toProductDetailModel(categoryMap: Map<Int, CategoryModel>) =
    ProductDetailModel(
        productId = productId,
        price = price,
        title = title,
        category = categories.mapNotNull { categoryMap[it]?.name },
        content = description,
        createdAt = createdAt,
        imgUrlList = listOfNotNull(thumbnailImgUrl), // 서버에서 아직 이미지 여러 장을 지원하지 않음
        minPeriod = period?.min,
        maxPeriod = period?.max
    )