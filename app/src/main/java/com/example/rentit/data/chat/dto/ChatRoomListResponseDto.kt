package com.example.rentit.data.chat.dto

import com.google.gson.annotations.SerializedName

data class ChatRoomListResponseDto(
    @SerializedName("chatrooms")
    val chatrooms: List<ChatRoomDataDto>,

    @SerializedName("hasNext")
    val hasNext: Boolean,

    @SerializedName("totalPage")
    val totalPage: Int
)

data class ChatRoomDataDto(
    @SerializedName("chatroomId")
    val chatroomId: String,

    @SerializedName("productTitle")
    val productTitle: String,

    @SerializedName("partnerNickname")
    val partnerNickname: String,

    @SerializedName("lastMessage")
    val lastMessage: String,

    @SerializedName("lastMessageTime")
    val lastMessageTime: String,
)