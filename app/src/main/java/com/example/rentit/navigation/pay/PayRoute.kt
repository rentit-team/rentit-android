package com.example.rentit.navigation.pay

import kotlinx.serialization.Serializable

sealed class PayRoute {
    @Serializable
    data class Pay(val productId: Int, val reservationId: Int, val payInfoJson: String) : PayRoute()
}