package com.example.rentit.navigation.auth

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.rentit.presentation.auth.join.JoinScreen
import com.example.rentit.presentation.auth.login.LoginScreen
import com.example.rentit.presentation.main.MainView

fun NavHostController.navigateToLogin(
) {
    navigate(
        route = AuthRoute.Login
    )
}

fun NavHostController.navigateToJoin(
    name: String?,
    email: String?
) {
    navigate(
        route = AuthRoute.Join(name, email)
    )
}

fun NavHostController.navigateToMain(
) {
    navigate(
        route = AuthRoute.Main
    )
}


@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.authGraph(navHostController: NavHostController) {
    composable<AuthRoute.Login> { LoginScreen(navHostController) }

    composable<AuthRoute.Join> { backStackEntry ->
        val items: AuthRoute.Join = backStackEntry.toRoute()
        JoinScreen(navHostController, items.name, items.email)
    }

    composable<AuthRoute.Main> { MainView() }
}