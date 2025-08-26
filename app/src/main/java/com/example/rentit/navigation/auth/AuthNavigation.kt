package com.example.rentit.navigation.auth

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.rentit.presentation.auth.join.nickname.JoinNicknameRoute
import com.example.rentit.presentation.auth.join.phoneverification.JoinPhoneVerificationRoute
import com.example.rentit.presentation.auth.login.LoginScreen

fun NavHostController.navigateToLogin(
) {
    navigate(
        route = AuthRoute.Login
    ) {
        popUpTo(graph.id){
            inclusive = true
        }
        launchSingleTop = true
    }
}


fun NavHostController.navigateToJoinPhoneVerification(
    name: String?,
    email: String?,
) {
    navigate(
        route = AuthRoute.JoinPhoneVerification(name, email)
    )
}


fun NavHostController.navigateToJoinNickname(
    name: String?,
    email: String?,
) {
    navigate(
        route = AuthRoute.JoinNickname(name, email)
    )
}


@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.authGraph(navHostController: NavHostController) {
    composable<AuthRoute.Login> { LoginScreen(navHostController) }

    composable<AuthRoute.JoinPhoneVerification> { backStackEntry ->
        val items: AuthRoute.JoinPhoneVerification = backStackEntry.toRoute()
        JoinPhoneVerificationRoute(navHostController, items.name, items.email)
    }

    composable<AuthRoute.JoinNickname> { backStackEntry ->
        val items: AuthRoute.JoinNickname = backStackEntry.toRoute()
        JoinNicknameRoute(navHostController, items.name, items.email)
    }
}