package com.example.rentit

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.rentit.common.component.NavigationRoutes
import com.example.rentit.common.component.moveScreen
import com.example.rentit.common.theme.Gray400
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.feature.auth.AuthViewModel
import com.example.rentit.feature.auth.JoinScreen
import com.example.rentit.feature.auth.LoginScreen
import com.example.rentit.feature.chat.ChatListScreen
import com.example.rentit.feature.home.HomeScreen
import com.example.rentit.feature.mypage.MyPageScreen
import com.example.rentit.feature.product.BookingRequestScreen
import com.example.rentit.feature.product.ProductDetailScreen
import dagger.hilt.android.AndroidEntryPoint

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

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RentItTheme {
                //LoginNavHost(authViewModel)
                BookingRequestScreen()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        authViewModel.handleGoogleSignInResult(requestCode, resultCode, data)
    }
}

@Composable
fun LoginNavHost(authViewModel: AuthViewModel){
    val navHostController = rememberNavController()
    NavHost(navController = navHostController, startDestination = NavigationRoutes.LOGIN){
        composable(NavigationRoutes.LOGIN) { LoginScreen(authViewModel, navHostController) }
        composable(NavigationRoutes.JOIN) { JoinScreen(authViewModel, navHostController) }
        composable(NavigationRoutes.MAIN) { MainView() }
    }
}

@Composable
fun TabNavHost(navHostController: NavHostController, paddingValues: PaddingValues) {
    // Create NavGraph - 이동할 Composable 대상을 매핑
    // NavHost - NavGraph의 현재 대상을 표시하는 컨테이너 역할의 Composable
    // TopBar, BottomBar 등에 UI가 가려지지 않도록 padding으로 안전한 영역 확보
    NavHost(navController = navHostController, startDestination = BottomNavItem.Home.screenRoute, modifier = Modifier.padding(paddingValues)){
        composable(BottomNavItem.Home.screenRoute) { HomeScreen() }
        composable(BottomNavItem.Chat.screenRoute) { ChatListScreen("ChatListScreen") }
        composable(BottomNavItem.MyPage.screenRoute) { MyPageScreen("MyPageScreen") }
    }
}

@Composable
fun MainView() {
    val navHostController = rememberNavController()
    Scaffold(bottomBar = {
        BottomNavigation(backgroundColor = Color.White, modifier = Modifier.height(72.dp)) {
            val navBackStackEntry by navHostController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            navItems.forEach { item ->
                BottomNavigationItem(
                    modifier = Modifier.fillMaxHeight(),
                    icon = {
                        val icon = if(currentRoute == item.screenRoute) item.iconSelected else item.icon
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
                    onClick = { moveScreen(navHostController, item.screenRoute, saveStateEnabled = false, restoreStateEnabled = true) },
                )}
        }
    }){
        TabNavHost(navHostController, it)
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    RentItTheme {
        MainView()
    }
}