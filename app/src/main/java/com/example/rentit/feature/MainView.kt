package com.example.rentit.feature

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.rentit.R
import com.example.rentit.common.component.NavigationRoutes
import com.example.rentit.common.component.moveScreen
import com.example.rentit.common.theme.Gray400
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.feature.chat.ChatListScreen
import com.example.rentit.feature.home.HomeScreen
import com.example.rentit.feature.user.MyPageScreen
import com.example.rentit.feature.product.BookingRequestScreen
import com.example.rentit.feature.product.CreatePostScreen
import com.example.rentit.feature.product.ProductDetailScreen
import com.example.rentit.feature.product.ProductViewModel
import com.example.rentit.feature.product.RequestConfirmationScreen
import com.example.rentit.feature.user.RequestHistoryScreen

sealed class BottomNavItem(
    val title: Int, val icon: Int, val iconSelected: Int, val screenRoute: String
){
    data object Home: BottomNavItem(R.string.title_activity_home_tab, R.drawable.ic_home, R.drawable.ic_home_fill, NavigationRoutes.HOME)
    data object Chat: BottomNavItem(R.string.title_activity_chat_tab, R.drawable.ic_chat, R.drawable.ic_chat_fill, NavigationRoutes.CHAT)
    data object MyPage: BottomNavItem(R.string.title_activity_my_page_tab, R.drawable.ic_user, R.drawable.ic_user_fill, NavigationRoutes.MYPAGE)
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
            moveScreen(navHostController, NavigationRoutes.CREATEPOST)
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
        composable(BottomNavItem.Chat.screenRoute) { ChatListScreen("ChatListScreen") }
        composable(BottomNavItem.MyPage.screenRoute) { MyPageScreen(navHostController) }
        composable(NavigationRoutes.NAVHOSTPRODUCTDETAIL+"/{productId}", arguments = listOf(navArgument("productId") { type = NavType.IntType })) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId")
            ProductDetailNavHost(productId) }
        composable(NavigationRoutes.NAVHOSTMYPRODUCTDETAIL+"/{productId}", arguments = listOf(navArgument("productId") { type = NavType.IntType })) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId")
            MyProductDetailNavHost(productId) }
        composable(NavigationRoutes.CREATEPOST) { CreatePostNavHost() }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProductDetailNavHost(productId: Int?) {
    val navHostController: NavHostController = rememberNavController()
    val productViewModel: ProductViewModel = hiltViewModel()

    LaunchedEffect(productId) {
        if (productId != null) {
            productViewModel.setProductId(productId)
        }
    }

    NavHost(navController =  navHostController, startDestination = NavigationRoutes.PRODUCTDETAIL){
        composable(NavigationRoutes.PRODUCTDETAIL) { ProductDetailScreen(navHostController, productViewModel) }
        composable(NavigationRoutes.BOOKINGREQUEST) { BookingRequestScreen(navHostController, productViewModel) }
        composable(NavigationRoutes.REQUESTCONFIRM) { RequestConfirmationScreen(navHostController, productViewModel) }
        composable(NavigationRoutes.MAIN) { MainView() }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyProductDetailNavHost(productId: Int?) {
    val navHostController: NavHostController = rememberNavController()
    val productViewModel: ProductViewModel = hiltViewModel()

    LaunchedEffect(productId) {
        if (productId != null) {
            productViewModel.setProductId(productId)
        }
    }

    NavHost(navController =  navHostController, startDestination = NavigationRoutes.PRODUCTDETAIL){
        composable(NavigationRoutes.PRODUCTDETAIL) { ProductDetailScreen(navHostController, productViewModel) }
        composable(NavigationRoutes.REQUESTHISTORY) { RequestHistoryScreen(navHostController, productViewModel) }
        composable(NavigationRoutes.MAIN) { MainView() }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreatePostNavHost() {
    val navHostController: NavHostController = rememberNavController()
    NavHost(navController =  navHostController, startDestination = NavigationRoutes.CREATEPOST){
        composable(NavigationRoutes.CREATEPOST) { CreatePostScreen(navHostController) }
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
