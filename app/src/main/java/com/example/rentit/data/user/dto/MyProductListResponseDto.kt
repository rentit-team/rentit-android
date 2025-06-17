package com.example.rentit.data.user.dto

import com.example.rentit.data.product.dto.ProductDto
import com.google.gson.annotations.SerializedName

data class MyProductListResponseDto(
    @SerializedName("myProducts")
    val myProducts: List<ProductDto>,
)