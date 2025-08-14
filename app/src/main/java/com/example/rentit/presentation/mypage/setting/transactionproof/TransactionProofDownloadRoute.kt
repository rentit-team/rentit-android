package com.example.rentit.presentation.mypage.setting.transactionproof

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import com.example.rentit.common.theme.RentItTheme

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TransactionProofDownloadRoute() {

    RentItTheme {
        TransactionProofDownloadScreen(emptyList())
    }
}