package com.example.rentit.navigation.productdetail

import kotlinx.serialization.Serializable

sealed class ProductDetailRoute {
    @Serializable
    data class ProductDetail(val productId: Int) : ProductDetailRoute()

    @Serializable
    data class ResvRequest(val productId: Int?) : ProductDetailRoute()

    @Serializable
    data class ResvRequestComplete(
        val rentalStartDate: String,
        val rentalEndDate: String,
        val formattedTotalPrice: String,
    ) : ProductDetailRoute()

    @Serializable
    data class ResvRequestHistory(val productId: Int?) : ProductDetailRoute()
}