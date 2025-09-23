package com.example.rentit.presentation.chat.chatroom

import com.example.rentit.domain.chat.model.ChatMessageModel
import com.example.rentit.domain.chat.model.ChatRoomProductSummaryModel
import com.example.rentit.domain.chat.model.ChatRoomRentalSummaryModel

data class ChatRoomState(
    val messageText: String = "",
    val partnerNickname: String = "",
    val rentalSummary: ChatRoomRentalSummaryModel? = null,
    val productSummary: ChatRoomProductSummaryModel? = null,
    val messages: List<ChatMessageModel> = emptyList(),
    val isLoading: Boolean = false,
    val showForbiddenChatAccessDialog: Boolean = false
)