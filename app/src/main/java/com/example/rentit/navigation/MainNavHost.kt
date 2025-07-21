package com.example.rentit.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.rentit.navigation.bottomtab.BottomTabRoute
import com.example.rentit.navigation.bottomtab.bottomTabGraph
import com.example.rentit.navigation.chatroom.chatRoomGraph
import com.example.rentit.navigation.createpost.createPostGraph
import com.example.rentit.navigation.productdetail.productDetailGraph

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainNavHost(navHostController: NavHostController, paddingValues: PaddingValues) {
    NavHost(navController = navHostController, startDestination = BottomTabRoute.Home.route, modifier = Modifier.padding(paddingValues)){
        bottomTabGraph(navHostController)
        productDetailGraph(navHostController)
        chatRoomGraph(navHostController)
        createPostGraph(navHostController)
    }
}