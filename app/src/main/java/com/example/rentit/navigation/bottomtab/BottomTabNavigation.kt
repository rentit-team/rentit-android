package com.example.rentit.navigation.bottomtab

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.rentit.presentation.chat.ChatListScreen
import com.example.rentit.presentation.home.HomeScreen
import com.example.rentit.presentation.mypage.MyPageScreen

fun NavHostController.navigateBottomTab(route: String) {
    navigate(route = route) {
        popUpTo(BottomTabRoute.Home.route) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

fun NavHostController.navigateToHome() {
    navigate(BottomTabRoute.Home.route) {
        popUpTo(graph.id){
            inclusive = true
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.bottomTabGraph(navHostController: NavHostController) {
    composable(BottomTabRoute.Home.route) {
        HomeScreen(navHostController)
    }
    composable(BottomTabRoute.Chat.route) {
        ChatListScreen(navHostController)
    }
    composable(BottomTabRoute.MyPage.route) {
        MyPageScreen(navHostController)
    }
}
