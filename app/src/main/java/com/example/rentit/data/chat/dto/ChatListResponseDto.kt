package com.example.rentit.data.chat.dto

import com.google.gson.annotations.SerializedName

data class ChatListResponseDto(
    @SerializedName("chatrooms")
    val chatRooms: List<ChatRoomSummaryDto>,

    @SerializedName("hasNext")
    val hasNext: Boolean,

    @SerializedName("totalPage")
    val totalPage: Int
)

data class ChatRoomSummaryDto(
    @SerializedName("chatroomId")
    val chatRoomId: String,

    @SerializedName("productId")
    val productId: Int,

    @SerializedName("reservationId")
    val reservationId: Int,

    @SerializedName("productTitle")
    val productTitle: String,

    @SerializedName("partnerNickname")
    val partnerNickname: String,

    @SerializedName("lastMessage")
    val lastMessage: String,

    @SerializedName("lastMessageTime")
    val lastMessageTime: String?,
)