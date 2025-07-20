package com.example.rentit.navigation.chatroom

import kotlinx.serialization.Serializable

sealed class ChatRoomRoute {
    @Serializable
    data class ChatRoom(val productId: Int?, val reservationId: Int?, val chatRoomId: String?) : ChatRoomRoute()

    @Serializable
    data object RequestAcceptConfirm : ChatRoomRoute()
}