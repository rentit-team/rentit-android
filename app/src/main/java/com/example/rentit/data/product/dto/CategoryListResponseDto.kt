package com.example.rentit.data.product.dto

import com.google.gson.annotations.SerializedName

data class CategoryListResponseDto(

    @SerializedName("categories")
    val categories: List<CategoryDto>,
)

data class CategoryDto(

    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("isParent")
    val isParent: Boolean,

    @SerializedName("parentId")
    val parentId: Int?,
)