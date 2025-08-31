package com.example.rentit

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.navigation.compose.rememberNavController
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.presentation.chat.chatroom.ChatroomRoute
import com.example.rentit.presentation.main.MainRoute
import com.example.rentit.presentation.rentaldetail.owner.OwnerRentalDetailRoute
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RentItTheme {
                OwnerRentalDetailRoute(rememberNavController(), 21, 40)
            }
        }
    }
}


