package com.example.rentit.data.product.dto

import com.google.gson.annotations.SerializedName

data class ProductListResponseDto(
    @SerializedName("products")
    val products: List<ProductDto>,

    @SerializedName("hasNext")
    val hasNext: Boolean,

    @SerializedName("totalPage")
    val totalPage: Int
)

data class ProductDto(
    @SerializedName("productId")
    val productId: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("price")
    val price: Int,

    @SerializedName("thumbnailUrl")
    val thumbnailUrl: String?,

    @SerializedName("region")
    val region: String?,

    @SerializedName("period")
    val period: PeriodDto,

    @SerializedName("owner")
    val owner: OwnerDto,

    @SerializedName("status")
    val status: String,

    @SerializedName("createdAt")
    val createdAt: String // or LocalDateTime if using a converter
)

data class PeriodDto(
    @SerializedName("cycle")
    val cycle: String?,

    @SerializedName("min")
    val min: Int?,

    @SerializedName("max")
    val max: Int?
)

data class OwnerDto(
    @SerializedName("userId")
    val userId: Int,

    @SerializedName("nickname")
    val nickname: String
)
