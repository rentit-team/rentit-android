package com.example.rentit.navigation.setting

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.rentit.presentation.mypage.setting.SettingRoute
import com.example.rentit.presentation.mypage.setting.transactionproof.TransactionProofDownloadRoute

fun NavHostController.navigateToSetting() {
    navigate(
        route = SettingRoute.Setting
    )
}

fun NavHostController.navigateToTransactionProof() {
    navigate(
        route = SettingRoute.TransactionProof
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.settingNavGraph(navHostController: NavHostController) {
    composable<SettingRoute.Setting> { SettingRoute(navHostController) }
    composable<SettingRoute.TransactionProof> { TransactionProofDownloadRoute(navHostController) }
}