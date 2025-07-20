package com.example.rentit.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.rentit.presentation.chat.chatroom.ChatroomScreen
import com.example.rentit.presentation.chat.chatroom.requestaccept.confirm.RequestAcceptConfirmScreen

fun NavHostController.navigateToChatRoom(
    productId: Int?,
    reservationId: Int?,
    chatRoomId: String?,
) {
    navigate(
        route = ChatRoomRoute.ChatRoom(productId, reservationId, chatRoomId)
    )
}

fun NavHostController.navigateToRequestAcceptConfirm(
) {
    navigate(
        route = ChatRoomRoute.RequestAcceptConfirm
    )
}


@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.chatRoomGraph(navHostController: NavHostController) {
    composable<ChatRoomRoute.ChatRoom> { backStackEntry ->
        val items: ChatRoomRoute.ChatRoom = backStackEntry.toRoute()
        ChatroomScreen(
            navHostController,
            items.productId,
            items.reservationId,
            items.chatRoomId
        )
    }
    composable<ChatRoomRoute.RequestAcceptConfirm> { RequestAcceptConfirmScreen(navHostController) }
}