package com.example.rentit.common.component.layout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.rentit.common.theme.PrimaryBlue500

@Composable
fun RentItLoadingScreen(isLoading: Boolean = true) {
    AnimatedVisibility(
        visible = isLoading,
        enter = EnterTransition.None,
        exit = ExitTransition.None
    ) {
        Box(Modifier.fillMaxSize().background(Color.White.copy(alpha = 0.4f)),
            contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                modifier = Modifier.fillMaxWidth(0.08f),
                color = PrimaryBlue500
            )
        }
    }
}