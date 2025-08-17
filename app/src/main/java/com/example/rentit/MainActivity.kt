package com.example.rentit

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import com.example.rentit.common.storage.getToken
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.navigation.AuthNavHost
import com.example.rentit.presentation.main.MainRoute
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
                    AuthNavHost()
                } else {
                    MainRoute()
                }
            }
        }
    }
}


