package com.example.rentit.navigation

sealed class BottomTabRoute(val route: String) {

    data object Home : BottomTabRoute("home")

    data object Chat : BottomTabRoute("chat")

    data object MyPage : BottomTabRoute("my_page")
}