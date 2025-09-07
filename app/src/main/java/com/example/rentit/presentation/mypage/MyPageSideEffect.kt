package com.example.rentit.presentation.mypage

sealed class MyPageSideEffect {
    data class NavigateToProductDetail(val productId: Int): MyPageSideEffect()
    data class NavigateToRentalDetail(val productId: Int, val reservationId: Int): MyPageSideEffect()
    data object NavigateToSetting: MyPageSideEffect()
}