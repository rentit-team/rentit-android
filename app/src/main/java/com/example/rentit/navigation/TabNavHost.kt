package com.example.rentit.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.rentit.presentation.chat.ChatListScreen
import com.example.rentit.presentation.home.HomeScreen
import com.example.rentit.presentation.main.BottomNavItem
import com.example.rentit.presentation.mypage.MyPageScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TabNavHost(navHostController: NavHostController, paddingValues: PaddingValues) {
    // Create NavGraph - 이동할 Composable 대상을 매핑
    // NavHost - NavGraph의 현재 대상을 표시하는 컨테이너 역할의 Composable
    // TopBar, BottomBar 등에 UI가 가려지지 않도록 padding으로 안전한 영역 확보
    NavHost(navController = navHostController, startDestination = BottomNavItem.Home.screenRoute, modifier = Modifier.padding(paddingValues)){
        composable(BottomNavItem.Home.screenRoute) { HomeScreen(navHostController) }
        composable(BottomNavItem.Chat.screenRoute) { ChatListScreen(navHostController) }
        composable(BottomNavItem.MyPage.screenRoute) { MyPageScreen(navHostController) }
        composable(
            route = NavigationRoutes.PRODUCT_DETAIL_NAV_HOST+"/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.IntType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId")
            ProductDetailNavHost(productId)
        }
        composable(
            route = NavigationRoutes.CHAT_NAV_HOST + "/{productId}/{reservationId}/{chatRoomId}",
            arguments = listOf(
                navArgument("productId") { type = NavType.IntType },
                navArgument("reservationId") { type = NavType.IntType },
                navArgument("chatRoomId") { type = NavType.StringType })
        ) { backStackEntry ->
            val pId = backStackEntry.arguments?.getInt("productId")
            val rId = backStackEntry.arguments?.getInt("reservationId")
            val cId = backStackEntry.arguments?.getString("chatRoomId")
            ChatroomNavHost(pId, rId, cId)
        }
        composable(NavigationRoutes.CREATE_POST_NAV_HOST) { CreatePostNavHost() }
    }
}