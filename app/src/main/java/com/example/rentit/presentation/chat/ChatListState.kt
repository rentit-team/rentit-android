package com.example.rentit.presentation.chat

import androidx.compose.foundation.lazy.LazyListState
import com.example.rentit.domain.chat.model.ChatRoomSummaryModel

data class ChatListState(
    val chatRoomSummaries: List<ChatRoomSummaryModel> = emptyList(),
    val scrollState: LazyListState = LazyListState(),
    val isRefreshing: Boolean = false,
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