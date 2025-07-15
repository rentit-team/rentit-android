package com.example.rentit.common.enums

enum class ProductStatus(val label: String?) {
    AVAILABLE("대여 가능"),
    RENTED("대여중"),
    RESERVED("예약됨"),
    DISABLED(null)
}

