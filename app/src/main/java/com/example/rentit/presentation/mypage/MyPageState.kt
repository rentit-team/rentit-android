package com.example.rentit.presentation.mypage

import com.example.rentit.data.product.dto.ProductDto
import com.example.rentit.data.user.dto.ReservationDto

data class MyPageState(
    val myProductList: List<ProductDto> = emptyList(),
    val myRentalList: List<ReservationDto> = emptyList(),
    val isFirstTabSelected: Boolean = true,
    val isLoading: Boolean = false
)