package com.example.rentit.presentation.main.createpost

sealed class CreatePostSideEffect {
    data class NavigateToProductDetail(val productId: Int) : CreatePostSideEffect()
    data object ShowNetworkErrorToast : CreatePostSideEffect()
    data object ShowPostErrorToast : CreatePostSideEffect()
}