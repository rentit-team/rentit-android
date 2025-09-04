package com.example.rentit.presentation.chat.chatroom

sealed class ChatRoomSideEffect {
    data object ToastChatDisconnect : ChatRoomSideEffect()
    data object ToastMessageSendFailed : ChatRoomSideEffect()
}