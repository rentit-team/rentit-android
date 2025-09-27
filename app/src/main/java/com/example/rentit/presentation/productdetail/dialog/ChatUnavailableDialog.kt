package com.example.rentit.presentation.productdetail.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.rentit.R
import com.example.rentit.common.ui.component.dialog.RentItBaseDialog

@Composable
fun ChatUnavailableDialog(
    onDismiss: () -> Unit
) {
    RentItBaseDialog(
        title = stringResource(R.string.dialog_chat_unavailable_title),
        content = stringResource(R.string.dialog_chat_unavailable_content),
        confirmBtnText = stringResource(R.string.common_dialog_btn_close),
        onDismissRequest = onDismiss,
        onConfirmRequest = onDismiss,
    )
}