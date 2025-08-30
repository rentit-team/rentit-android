package com.example.rentit.common.component.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.rentit.R

@Composable
fun ServerErrorDialog(onRetry: () -> Unit = {}) {
    BaseDialog(
        title = stringResource(R.string.dialog_server_error_title),
        content = stringResource(R.string.dialog_server_error_content),
        confirmBtnText = stringResource(R.string.dialog_server_error_retry_btn),
        isBackgroundClickable = false,
        onConfirmRequest = onRetry,
    )
}