package com.example.rentit.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.rentit.navigation.auth.authGraph
import com.example.rentit.navigation.bottomtab.bottomTabGraph
import com.example.rentit.navigation.chatroom.chatRoomGraph
import com.example.rentit.navigation.createpost.createPostGraph
import com.example.rentit.navigation.pay.payGraph
import com.example.rentit.navigation.productdetail.productDetailGraph
import com.example.rentit.navigation.rentaldetail.rentalDetailGraph
import com.example.rentit.navigation.setting.settingNavGraph
import com.example.rentit.navigation.splash.SplashRoute
import com.example.rentit.navigation.splash.splashGraph

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainNavHost(navHostController: NavHostController, paddingValues: PaddingValues) {
    NavHost(navController = navHostController, startDestination = SplashRoute.Splash, modifier = Modifier.padding(paddingValues)){
        splashGraph(navHostController)
        authGraph(navHostController)
        bottomTabGraph(navHostController)
        productDetailGraph(navHostController)
        rentalDetailGraph(navHostController)
        chatRoomGraph(navHostController)
        createPostGraph(navHostController)
        payGraph(navHostController)
        settingNavGraph(navHostController)
    }
}