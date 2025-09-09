package com.example.rentit.presentation.chat.chatroom

sealed class ChatRoomSideEffect {
    data object MessageSendSuccess : ChatRoomSideEffect()
    data object MessageReceived : ChatRoomSideEffect()
    data object ToastChatDisconnect : ChatRoomSideEffect()
    data object ToastMessageSendFailed : ChatRoomSideEffect()
    data object ToastPaymentInvalidStatus : ChatRoomSideEffect()
    data object ToastPaymentNotRenter : ChatRoomSideEffect()
    data object ToastPaymentProductNotFound : ChatRoomSideEffect()
    data class NavigateToPay(val productId: Int, val reservationId: Int) : ChatRoomSideEffect()
    data class NavigateToProductDetail(val productId: Int) : ChatRoomSideEffect()
    data class NavigateToRentalDetail(val productId: Int, val reservationId: Int) : ChatRoomSideEffect()
    data class CommonError(val throwable: Throwable) : ChatRoomSideEffect()
}