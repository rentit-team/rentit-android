package com.example.rentit.presentation.main

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.rentit.common.component.dialog.NetworkErrorDialog
import com.example.rentit.common.component.dialog.ServerErrorDialog
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

    navBackStackEntry?.let { entry ->
        val viewModel: MainViewModel = hiltViewModel(entry)
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        if(uiState.showNetworkErrorDialog) {
            NetworkErrorDialog(
                navigateBack = navHostController::popBackStack,
                onRetry = viewModel::retry,
            )
        }

        if(uiState.showServerErrorDialog) {
            ServerErrorDialog(
                navigateBack = navHostController::popBackStack,
                onRetry = viewModel::retry
            )
        }
    }

    MainScreen(
        navHostController = navHostController,
        currentRoute = currentRoute,
        showBottomBar = showBottomBar,
        showCreatePostBtn = isHome,
        onBottomItemClick = navHostController::navigateBottomTab,
        onCreatePostClick = navHostController::navigateToCreatePost
    )
}