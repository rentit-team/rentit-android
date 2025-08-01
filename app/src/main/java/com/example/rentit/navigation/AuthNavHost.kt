package com.example.rentit.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.rentit.navigation.auth.AuthRoute
import com.example.rentit.navigation.auth.authGraph

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AuthNavHost(){
    val navHostController = rememberNavController()
    NavHost(navController = navHostController, startDestination = AuthRoute.Login){
        authGraph(navHostController)
    }
}