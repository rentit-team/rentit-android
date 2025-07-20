package com.example.rentit.presentation.main

import com.example.rentit.R
import com.example.rentit.navigation.BottomTabRoute

enum class BottomNavItem(
    val title: Int, val icon: Int, val iconSelected: Int, val screenRoute: String,
) {
    Home(
        R.string.title_activity_home_tab, R.drawable.ic_home, R.drawable.ic_home_fill,
        BottomTabRoute.Home.route
    ),
    Chat(
        R.string.title_activity_chat_tab, R.drawable.ic_chat, R.drawable.ic_chat_fill,
        BottomTabRoute.Chat.route
    ),
    MyPage(
        R.string.title_activity_my_page_tab, R.drawable.ic_user, R.drawable.ic_user_fill,
        BottomTabRoute.MyPage.route
    )
}
