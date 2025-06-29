package com.example.rentit.data.chat.dto

import com.google.gson.annotations.SerializedName

data class NewChatResponseDto(
    @SerializedName("data")
    val data: ChatRoomInfoDto,
)

data class ChatRoomInfoDto(
    @SerializedName("chatroomId")
    val chatRoomId: String,

    @SerializedName("participants")
    val participants: List<ChatParticipantInfoDto>
)

data class ChatParticipantInfoDto(
    @SerializedName("userId")
    val userId: Int,

    @SerializedName("nickname")
    val nickname: String
)