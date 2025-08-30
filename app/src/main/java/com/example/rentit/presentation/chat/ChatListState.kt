package com.example.rentit.presentation.chat

import com.example.rentit.domain.chat.model.ChatRoomSummaryModel

data class ChatListState(
    val chatRoomSummaries: List<ChatRoomSummaryModel> = emptyList(),
    val isLoading: Boolean = false,
    val showNetworkErrorDialog: Boolean = false,
    val showServerErrorDialog: Boolean = false
)