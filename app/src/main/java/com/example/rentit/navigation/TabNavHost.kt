package com.example.rentit.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.rentit.presentation.chat.ChatListScreen
import com.example.rentit.presentation.home.HomeScreen
import com.example.rentit.presentation.main.BottomNavItem
import com.example.rentit.presentation.mypage.MyPageScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TabNavHost(navHostController: NavHostController, paddingValues: PaddingValues) {
    NavHost(navController = navHostController, startDestination = BottomNavItem.Home.screenRoute, modifier = Modifier.padding(paddingValues)){
        composable(BottomNavItem.Home.screenRoute) { HomeScreen(navHostController) }
        composable(BottomNavItem.Chat.screenRoute) { ChatListScreen(navHostController) }
        composable(BottomNavItem.MyPage.screenRoute) { MyPageScreen(navHostController) }
        productDetailGraph(navHostController)
        chatRoomGraph(navHostController)
        createPostGraph(navHostController)
    }
}