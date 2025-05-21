package com.example.rentit

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rentit.common.component.NavigationRoutes
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.feature.MainView
import com.example.rentit.feature.auth.AuthViewModel
import com.example.rentit.feature.auth.JoinScreen
import com.example.rentit.feature.auth.LoginScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RentItTheme {
                LoginNavHost(authViewModel)
                //MainView()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        authViewModel.handleGoogleSignInResult(requestCode, resultCode, data)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LoginNavHost(authViewModel: AuthViewModel){
    val navHostController = rememberNavController()
    NavHost(navController = navHostController, startDestination = NavigationRoutes.LOGIN){
        composable(NavigationRoutes.LOGIN) { LoginScreen(authViewModel, navHostController) }
        composable(NavigationRoutes.JOIN) { JoinScreen(authViewModel, navHostController) }
        composable(NavigationRoutes.MAIN) { MainView() }
    }
}


