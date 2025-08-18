package com.example.rentit.presentation.main

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.rentit.navigation.bottomtab.navigateBottomTab
import com.example.rentit.navigation.createpost.navigateToCreatePost

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainRoute() {
    val navHostController = rememberNavController()
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = BottomNavItem.entries.any { it.screenRoute == currentRoute }
    val isHome = currentRoute == BottomNavItem.Home.screenRoute

    MainScreen(
        navHostController = navHostController,
        currentRoute = currentRoute,
        showBottomBar = showBottomBar,
        showCreatePostBtn = isHome,
        onBottomItemClick = navHostController::navigateBottomTab,
        onCreatePostClick = navHostController::navigateToCreatePost
    )
}