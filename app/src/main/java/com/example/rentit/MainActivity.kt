package com.example.rentit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.presentation.main.MainRoute
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RentItTheme {
                MainRoute()
            }
        }
    }
}


