package com.example.rentit.presentation.pay.payresult

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.rentit.R
import com.example.rentit.common.component.dialog.BaseDialog
import com.example.rentit.common.theme.Gray800

@Composable
fun PayResultDialog(
    model: PayResultDialogUiModel,
    onClose: () -> Unit = {},
    onConfirm: () -> Unit = {},
) {
    BaseDialog(
        title = model.titleText,
        content = model.contentText,
        confirmBtnText = stringResource(R.string.dialog_pay_result_btn_confirm),
        isBackgroundClickable = false,
        onCloseRequest = onClose,
        onConfirmRequest = onConfirm,
    )
}