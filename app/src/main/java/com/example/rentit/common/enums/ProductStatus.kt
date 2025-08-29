package com.example.rentit.common.enums

import androidx.compose.ui.graphics.Color
import com.example.rentit.common.theme.AppGreen
import com.example.rentit.common.theme.Gray400
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.theme.SecondaryYellow

enum class ProductStatus(val label: String?, val color: Color) {
    AVAILABLE("대여 가능", PrimaryBlue500),
    RENTED("대여중", SecondaryYellow),
    RESERVED("예약됨", AppGreen),
    DISABLED(null, Gray400)
}

