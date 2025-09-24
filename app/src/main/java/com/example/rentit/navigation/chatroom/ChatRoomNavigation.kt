package com.example.rentit.navigation.chatroom

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.rentit.presentation.chat.chatroom.ChatroomRoute

fun NavHostController.navigateToChatRoom(
    chatRoomId: String,
) {
    navigate(
        route = ChatRoomRoute.ChatRoom(chatRoomId)
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.chatRoomGraph(navHostController: NavHostController) {
    composable<ChatRoomRoute.ChatRoom> { backStackEntry ->
        val items: ChatRoomRoute.ChatRoom = backStackEntry.toRoute()
        ChatroomRoute(
            navHostController,
            items.chatRoomId
        )
    }
}