package com.example.rentit.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rentit.presentation.main.MainView
import com.example.rentit.presentation.home.createpost.CreatePostScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreatePostNavHost() {
    val navHostController: NavHostController = rememberNavController()
    NavHost(navController =  navHostController, startDestination = NavigationRoutes.CREATE_POST){
        composable(NavigationRoutes.CREATE_POST) { CreatePostScreen(navHostController) }
        composable(NavigationRoutes.MAIN) { MainView() }
    }
}