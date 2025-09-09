package com.example.rentit.presentation.chat

sealed class ChatListSideEffect {
    data class CommonError(val throwable: Throwable): ChatListSideEffect()
}