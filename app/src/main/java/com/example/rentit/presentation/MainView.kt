package com.example.rentit.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.rentit.R
import com.example.rentit.common.navigation.NavigationRoutes
import com.example.rentit.common.navigation.moveScreen
import com.example.rentit.common.theme.Gray400
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.presentation.chat.chatroom.requestaccept.confirm.RequestAcceptConfirmScreen
import com.example.rentit.presentation.chat.ChatListScreen
import com.example.rentit.presentation.chat.chatroom.ChatroomScreen
import com.example.rentit.presentation.home.HomeScreen
import com.example.rentit.presentation.mypage.MyPageScreen
import com.example.rentit.presentation.productdetail.reservation.request.ResvRequestScreen
import com.example.rentit.presentation.home.createpost.CreatePostScreen
import com.example.rentit.presentation.productdetail.ProductDetailScreen
import com.example.rentit.presentation.productdetail.reservation.request.complete.ResvRequestCompleteScreen
import com.example.rentit.presentation.productdetail.reservation.requesthistory.RequestHistoryScreen

sealed class BottomNavItem(
    val title: Int, val icon: Int, val iconSelected: Int, val screenRoute: String
){
    data object Home: BottomNavItem(R.string.title_activity_home_tab, R.drawable.ic_home, R.drawable.ic_home_fill, NavigationRoutes.HOME)
    data object Chat: BottomNavItem(R.string.title_activity_chat_tab, R.drawable.ic_chat, R.drawable.ic_chat_fill, NavigationRoutes.CHAT)
    data object MyPage: BottomNavItem(R.string.title_activity_my_page_tab, R.drawable.ic_user, R.drawable.ic_user_fill, NavigationRoutes.MY_PAGE)
}

val navItems = listOf(
    BottomNavItem.Home,
    BottomNavItem.Chat,
    BottomNavItem.MyPage
)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainView() {
    val navHostController = rememberNavController()
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showBottomBar = currentRoute in navItems.map { it.screenRoute }
    Scaffold(bottomBar = { if (showBottomBar) {
        BottomNavigation(backgroundColor = Color.White, modifier = Modifier.height(72.dp)) {
            navItems.forEach { item ->
                BottomNavigationItem(
                    modifier = Modifier.fillMaxHeight(),
                    icon = {
                        val icon =
                            if (currentRoute == item.screenRoute) item.iconSelected else item.icon
                        Icon(
                            painter = painterResource(id = icon),
                            contentDescription = stringResource(
                                id = item.title
                            ),
                            modifier = Modifier
                                .width(28.dp)
                                .height(28.dp)
                        )
                    },
                    unselectedContentColor = Gray400,
                    selectedContentColor = PrimaryBlue500,
                    selected = currentRoute == item.screenRoute,
                    alwaysShowLabel = false,
                    onClick = {
                        moveScreen(
                            navHostController,
                            item.screenRoute,
                            restoreStateEnabled = true
                        )
                    },
                )
            }
        }
    }
    },
        floatingActionButton = { if(currentRoute == NavigationRoutes.HOME) CreatePostFloatingButton {
            moveScreen(navHostController, NavigationRoutes.CREATE_POST_NAV_HOST)
        } }) {
        TabNavHost(navHostController, it)
    }
}

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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProductDetailNavHost(productId: Int?) {
    val navHostController: NavHostController = rememberNavController()

    NavHost(navController =  navHostController, startDestination = NavigationRoutes.PRODUCT_DETAIL){
        composable(NavigationRoutes.PRODUCT_DETAIL) { ProductDetailScreen(navHostController, productId) }
        composable(NavigationRoutes.RESV_REQUEST) { ResvRequestScreen(navHostController, productId) }
        composable(
            route = NavigationRoutes.RESV_REQUEST_COMPLETE + "/{rentalStartDate}/{rentalEndDate}/{rentalPeriod}/{formattedTotalPrice}",
            arguments = listOf(
                navArgument("rentalStartDate") { type = NavType.StringType },
                navArgument("rentalEndDate") { type = NavType.StringType },
                navArgument("rentalPeriod") { type = NavType.IntType },
                navArgument("formattedTotalPrice") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val rentalStartDate = backStackEntry.arguments?.getString("rentalStartDate") ?: ""
            val rentalEndDate = backStackEntry.arguments?.getString("rentalEndDate") ?: ""
            val rentalPeriod = backStackEntry.arguments?.getInt("rentalPeriod") ?: 0
            val formattedTotalPrice = backStackEntry.arguments?.getString("formattedTotalPrice") ?: "0"
            ResvRequestCompleteScreen(navHostController, rentalStartDate, rentalEndDate, rentalPeriod, formattedTotalPrice) }
        composable(NavigationRoutes.RESV_REQUEST_HISTORY) { RequestHistoryScreen(navHostController, productId) }
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
        composable(NavigationRoutes.MAIN) { MainView() }
    }
}

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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreatePostNavHost() {
    val navHostController: NavHostController = rememberNavController()
    NavHost(navController =  navHostController, startDestination = NavigationRoutes.CREATE_POST){
        composable(NavigationRoutes.CREATE_POST) { CreatePostScreen(navHostController) }
        composable(NavigationRoutes.MAIN) { MainView() }
    }
}

@Composable
fun CreatePostFloatingButton(onClick: () -> Unit) {
    FloatingActionButton(
        backgroundColor = PrimaryBlue500,
        onClick = onClick,
    ) {
        Image(
            modifier = Modifier.padding(14.dp),
            painter = painterResource(id = R.drawable.ic_write),
            contentDescription = "상품 등록하기"
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewMainView() {
    RentItTheme {
        MainView()
    }
}
