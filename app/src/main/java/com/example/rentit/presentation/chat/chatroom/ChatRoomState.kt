package com.example.rentit.presentation.chat.chatroom

import com.example.rentit.domain.chat.model.ChatMessageModel
import com.example.rentit.domain.product.model.ProductChatRoomSummaryModel
import com.example.rentit.domain.rental.model.RentalChatRoomSummaryModel

data class ChatRoomState(
    val messageText: String = "",
    val partnerNickname: String = "",
    val rentalSummary: RentalChatRoomSummaryModel? = null,
    val productSummary: ProductChatRoomSummaryModel? = null,
    val initialMessages: List<ChatMessageModel> = emptyList(),
    val realTimeMessages: List<ChatMessageModel> = emptyList(),
    val isLoading: Boolean = false
) {
    val messages: List<ChatMessageModel> = realTimeMessages + initialMessages
}