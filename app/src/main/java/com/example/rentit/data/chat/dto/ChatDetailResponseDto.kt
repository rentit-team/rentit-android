package com.example.rentit.data.chat.dto

import com.google.gson.annotations.SerializedName

data class ChatDetailResponseDto(
    @SerializedName("chatRoom")
    val chatRoom: ChatRoomDetailDto,

    @SerializedName("messages")
    val messages: List<ChatMessageDto>,

    @SerializedName("hasNext")
    val hasNext: Boolean,

    @SerializedName("totalPage")
    val totalPage: Int
)

data class ChatRoomDetailDto(
    @SerializedName("chatroomId")
    val chatroomId: String,

    @SerializedName("reservationId")
    val reservationId: Int,

    @SerializedName("productId")
    val productId: Int,

    @SerializedName("participants")
    val participants: List<ChatParticipantDto>,

    @SerializedName("statusHistory")
    val statusHistory: List<StatusHistoryDto>
)

data class ChatParticipantDto(
    @SerializedName("userId")
    val userId: Long,

    @SerializedName("nickname")
    val nickname: String,

    @SerializedName("role")
    val role: String, // e.g. "buyer", "seller"

    @SerializedName("profileImageUrl")
    val profileImageUrl: String
)

data class StatusHistoryDto(
    @SerializedName("status")
    val status: String, // e.g. "PENDING", "ACCEPTED", "COMPLETED"

    @SerializedName("changedAt")
    val changedAt: String,

    @SerializedName("changedBy")
    val changedBy: ChangedByDto
)

data class ChangedByDto(
    @SerializedName("userId")
    val userId: Int,

    @SerializedName("nickname")
    val nickname: String
)

data class ChatMessageDto(
    @SerializedName("messageId")
    val messageId: String,

    @SerializedName("sender")
    val sender: SenderDto,

    @SerializedName("content")
    val content: String,

    @SerializedName("sentAt")
    val sentAt: String,

    @SerializedName("type")
    val type: String,

    @SerializedName("isMine")
    val isMine: Boolean
)

data class SenderDto(
    @SerializedName("userId")
    val userId: Long,

    @SerializedName("nickname")
    val nickname: String
)