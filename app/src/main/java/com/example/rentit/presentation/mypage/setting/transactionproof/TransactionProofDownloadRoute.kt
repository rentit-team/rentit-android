package com.example.rentit.presentation.mypage.setting.transactionproof

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.rentit.common.theme.RentItTheme

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TransactionProofDownloadRoute(navHostController: NavHostController) {

    RentItTheme {
        TransactionProofDownloadScreen(
            emptyList(),
            onBackPressed = navHostController::popBackStack
        )
    }
}