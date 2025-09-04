package com.example.rentit.presentation.productdetail

sealed class ProductDetailSideEffect {
    data object NavigateToRentalHistory: ProductDetailSideEffect()
    data object NavigateToChatting: ProductDetailSideEffect()
    data object NavigateToResvRequest: ProductDetailSideEffect()
    data object ToastComingSoon: ProductDetailSideEffect()
}