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
    val showBottomBar = currentRoute in BottomNavItem.entries.map { it.screenRoute }

    MainScreen(
        navHostController = navHostController,
        currentRoute = currentRoute,
        showBottomBar = showBottomBar,
        showCreatePostBtn = currentRoute == BottomNavItem.Home.screenRoute,
        onBottomItemClick = { screenRoute ->
            navHostController.navigateBottomTab(screenRoute)
        },
        onCreatePostClick = { navHostController.navigateToCreatePost() }
    )
}