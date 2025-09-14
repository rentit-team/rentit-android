package com.example.rentit.presentation.mypage.myproductsrental

import com.example.rentit.common.enums.RentalStatus

sealed class MyProductsRentalSideEffect {
    data class NavigateToProductDetail(val productId: Int, val selectedReservationId: Int?, val rentalStatus: RentalStatus?): MyProductsRentalSideEffect()
    data class CommonError(val throwable: Throwable): MyProductsRentalSideEffect()
}