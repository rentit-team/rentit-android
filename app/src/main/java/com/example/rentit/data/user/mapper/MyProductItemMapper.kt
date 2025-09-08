package com.example.rentit.data.user.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.rentit.data.product.dto.ProductDto
import com.example.rentit.domain.product.model.Category
import com.example.rentit.domain.user.model.MyProductItemModel

@RequiresApi(Build.VERSION_CODES.O)
fun ProductDto.toMyProductItemModel(categoryMap: Map<Int, Category>) =
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