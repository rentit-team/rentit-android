package com.example.rentit.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TabNavHost(navHostController: NavHostController, paddingValues: PaddingValues) {
    NavHost(navController = navHostController, startDestination = BottomTabRoute.Home.route, modifier = Modifier.padding(paddingValues)){
        bottomTabGraph(navHostController)
        productDetailGraph(navHostController)
        chatRoomGraph(navHostController)
        createPostGraph(navHostController)
    }
}