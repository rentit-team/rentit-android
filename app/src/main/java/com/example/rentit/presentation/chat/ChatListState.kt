package com.example.rentit.presentation.chat

import com.example.rentit.domain.chat.model.ChatRoomSummaryModel

data class ChatListState(
    val chatRoomSummaries: List<ChatRoomSummaryModel> = emptyList(),
    val isActiveChatRooms: Boolean = true,
    val isLoading: Boolean = false,
) {
    val activeChatRoomSummaries: List<ChatRoomSummaryModel>
        get() = if(isActiveChatRooms) {
            chatRoomSummaries.filter { it.lastMessage.isNotEmpty() }
        } else {
            chatRoomSummaries.filter { it.lastMessage.isEmpty() }
        }
}