package com.example.rentit.presentation.pay

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.example.rentit.R
import com.example.rentit.presentation.pay.payresult.PayResultDialogUiModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PayRoute(navHostController: NavHostController, payInfo: PayUiModel) {

    val sampleDialogModel = PayResultDialogUiModel(
        titleText = stringResource(R.string.dialog_pay_result_success_title),
        contentText = stringResource(R.string.dialog_pay_result_failed_content)
    )
    val scrollState = rememberScrollState()
    val isDialogVisible = remember { mutableStateOf(false) }

    PayScreen(
        navHostController = navHostController,
        payModel = payInfo,
        dialogModel = sampleDialogModel,
        isDialogVisible = isDialogVisible.value,
        scrollState = scrollState,
        onPayClick = { isDialogVisible.value = true },
        onDialogClose = { isDialogVisible.value = false },
        onDialogConfirm = { isDialogVisible.value = false }
    )
}