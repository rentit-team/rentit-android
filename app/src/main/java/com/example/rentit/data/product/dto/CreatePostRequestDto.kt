package com.example.rentit.data.product.dto

import com.google.gson.annotations.SerializedName

data class CreatePostRequestDto(
    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("categoryIds")
    val categoryIds: List<Int>,

    @SerializedName("period")
    val period: PeriodDto,

    @SerializedName("price")
    val price: Double,

    @SerializedName("region")
    val region: String?,
)