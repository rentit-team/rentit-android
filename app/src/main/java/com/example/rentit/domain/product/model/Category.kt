package com.example.rentit.domain.product.model

data class Category (
    val name: String,
    val isParent: Boolean,
    val parentId: Int?,
)