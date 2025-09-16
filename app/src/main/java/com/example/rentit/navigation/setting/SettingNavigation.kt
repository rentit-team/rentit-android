package com.example.rentit.navigation.setting

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.rentit.presentation.mypage.setting.SettingRoute

fun NavHostController.navigateToSetting() {
    navigate(
        route = SettingRoute.Setting
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.settingNavGraph(navHostController: NavHostController) {
    composable<SettingRoute.Setting> { SettingRoute(navHostController) }
}