package com.example.rentit.data.product.model

data class Category (
    val name: String,
    val isParent: Boolean,
    val parentId: Int?,
)