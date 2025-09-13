package com.example.rentit.presentation.mypage.myproductsrental

sealed class MyProductsRentalSideEffect {
    data class CommonError(val throwable: Throwable): MyProductsRentalSideEffect()
}