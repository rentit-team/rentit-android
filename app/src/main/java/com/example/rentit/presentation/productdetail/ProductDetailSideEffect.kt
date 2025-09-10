package com.example.rentit.presentation.productdetail

sealed class ProductDetailSideEffect {
    data object NavigateToRentalHistory: ProductDetailSideEffect()
    data class NavigateToChatting(val chatRoomId: String): ProductDetailSideEffect()
    data object NavigateToResvRequest: ProductDetailSideEffect()
    data object ToastComingSoon: ProductDetailSideEffect()
    data class CommonError(val throwable: Throwable): ProductDetailSideEffect()
}