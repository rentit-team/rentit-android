package com.example.rentit.navigation

import kotlinx.serialization.Serializable

sealed class ChatRoomRoute {
    @Serializable
    data class ChatRoom(val productId: Int?, val reservationId: Int?, val chatRoomId: String?) : ChatRoomRoute()

    @Serializable
    data object RequestAcceptConfirm : ChatRoomRoute()
}