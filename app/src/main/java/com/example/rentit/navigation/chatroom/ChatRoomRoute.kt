package com.example.rentit.navigation.chatroom

import kotlinx.serialization.Serializable

sealed class ChatRoomRoute {
    @Serializable
    data class ChatRoom(val chatRoomId: String) : ChatRoomRoute()

    @Serializable
    data object RequestAcceptConfirm : ChatRoomRoute()
}