package com.example.rentit.presentation.main

import com.example.rentit.R
import com.example.rentit.navigation.NavigationRoutes

sealed class BottomNavItem(
    val title: Int, val icon: Int, val iconSelected: Int, val screenRoute: String
){
    data object Home: BottomNavItem(R.string.title_activity_home_tab, R.drawable.ic_home, R.drawable.ic_home_fill,
        NavigationRoutes.HOME
    )
    data object Chat: BottomNavItem(R.string.title_activity_chat_tab, R.drawable.ic_chat, R.drawable.ic_chat_fill,
        NavigationRoutes.CHAT
    )
    data object MyPage: BottomNavItem(R.string.title_activity_my_page_tab, R.drawable.ic_user, R.drawable.ic_user_fill,
        NavigationRoutes.MY_PAGE
    )
}
