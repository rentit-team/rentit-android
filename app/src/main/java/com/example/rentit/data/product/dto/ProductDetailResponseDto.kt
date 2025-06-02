package com.example.rentit.data.product.dto

import com.google.gson.annotations.SerializedName

data class ProductDetailResponseDto(
    @SerializedName("product")
    val product: ProductDto
)