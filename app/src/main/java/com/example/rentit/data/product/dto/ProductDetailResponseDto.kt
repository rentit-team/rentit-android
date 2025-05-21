package com.example.rentit.data.product.dto

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class ProductDetailResponseDto(
    @SerializedName("product")
    val product: ProductDetailDto
)

data class ProductDetailDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("price")
    val price: Int,

    @SerializedName("thumbnailImgUrl")
    val thumbnailImgUrl: String,

    @SerializedName("region")
    val region: String,

    @SerializedName("period")
    val period: PeriodDto,

    @SerializedName("owner")
    val owner: OwnerDto,

    @SerializedName("categories")
    val categories: List<String>,

    @SerializedName("status")
    val status: String,

    @SerializedName("createdAt")
    val createdAt: String
)