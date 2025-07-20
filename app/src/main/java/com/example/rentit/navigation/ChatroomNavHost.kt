package com.example.rentit.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rentit.presentation.main.MainView
import com.example.rentit.presentation.chat.chatroom.ChatroomScreen
import com.example.rentit.presentation.chat.chatroom.requestaccept.confirm.RequestAcceptConfirmScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatroomNavHost(productId: Int?, reservationId: Int?, chatRoomId: String?) {
    val navHostController: NavHostController = rememberNavController()
    NavHost(navController =  navHostController, startDestination = NavigationRoutes.CHAT_ROOM){
        composable(NavigationRoutes.CHAT_ROOM) { ChatroomScreen(navHostController, productId, reservationId, chatRoomId) }
        composable(NavigationRoutes.REQUEST_ACCEPT_CONFIRM) { RequestAcceptConfirmScreen(navHostController) }
        composable(NavigationRoutes.MAIN) { MainView() }
    }
}