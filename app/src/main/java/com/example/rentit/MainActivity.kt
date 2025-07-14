package com.example.rentit

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.rentit.common.component.NavigationRoutes
import com.example.rentit.common.storage.getToken
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.feature.MainView
import com.example.rentit.feature.auth.join.JoinScreen
import com.example.rentit.feature.auth.login.LoginScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RentItTheme {
                val accessToken: String? = getToken(context = applicationContext)
                if(accessToken.isNullOrEmpty()) {
                    LoginNavHost()
                } else {
                    MainView()
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LoginNavHost(){
    val navHostController = rememberNavController()
    NavHost(navController = navHostController, startDestination = NavigationRoutes.LOGIN){
        composable(NavigationRoutes.LOGIN) { LoginScreen(navHostController) }
        composable(
            route = NavigationRoutes.JOIN + "/{name}/{email}",
            arguments = listOf(
                navArgument("name") { type = NavType.StringType },
                navArgument("email") { type = NavType.StringType }
            )) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name")
            val email = backStackEntry.arguments?.getString("email")
            JoinScreen(navHostController, name, email)
        }
        composable(NavigationRoutes.MAIN) { MainView() }
    }
}


