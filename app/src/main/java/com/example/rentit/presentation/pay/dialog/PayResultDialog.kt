package com.example.rentit.presentation.pay.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.rentit.R
import com.example.rentit.common.component.dialog.BaseDialog

@Composable
fun PayResultDialog(
    onClose: () -> Unit = {},
    onConfirm: () -> Unit = {},
) {
    BaseDialog(
        title = stringResource(R.string.dialog_pay_result_success_title),
        content = stringResource(R.string.dialog_pay_result_failed_content),
        confirmBtnText = stringResource(R.string.dialog_pay_result_btn_confirm),
        isBackgroundClickable = false,
        onCloseRequest = onClose,
        onConfirmRequest = onConfirm,
    )
}