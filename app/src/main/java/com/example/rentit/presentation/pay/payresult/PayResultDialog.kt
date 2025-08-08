package com.example.rentit.presentation.pay.payresult

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.rentit.R
import com.example.rentit.common.component.BaseDialog
import com.example.rentit.common.theme.Gray800

@Composable
fun PayResultDialog(
    dialogData: PayResultDialogUiModel,
    onClose: () -> Unit = {},
    onConfirm: () -> Unit = {},
) {
    BaseDialog(
        titleText = dialogData.titleText,
        confirmText = stringResource(R.string.dialog_pay_result_btn_confirm),
        onCloseRequest = onClose,
        onConfirmRequest = onConfirm
    ) {
        if (dialogData.contentText != null) {
            Text(text = dialogData.contentText, style = MaterialTheme.typography.labelMedium, color = Gray800)
        }
    }
}