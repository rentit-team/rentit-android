package com.example.rentit.presentation.mypage.setting

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.rentit.navigation.setting.navigateToTransactionProof

@Composable
fun SettingRoute(navHostController: NavHostController) {
    SettingScreen(
        onBackPressed = navHostController::popBackStack,
        onTransactionProofClick = navHostController::navigateToTransactionProof
    )
}