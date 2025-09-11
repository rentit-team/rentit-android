package com.example.rentit.data.product.dto

import com.google.gson.annotations.SerializedName

data class CreatePostResponseDto(
    @SerializedName("productId")
    val productId: Int,

    @SerializedName("createdAt")
    val createdAt: String,
)