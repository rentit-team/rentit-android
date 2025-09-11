package com.example.rentit.domain.product.model

data class CategoryModel (
    val name: String,
    val isParent: Boolean,
    val parentId: Int?,
)