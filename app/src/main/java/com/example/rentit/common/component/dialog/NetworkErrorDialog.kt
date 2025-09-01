package com.example.rentit.common.component.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.rentit.R

@Composable
fun NetworkErrorDialog(navigateBack: () -> Unit = {}, onRetry: () -> Unit = {}) {
    BaseDialog(
        title = stringResource(R.string.dialog_network_error_title),
        content = stringResource(R.string.dialog_network_error_content),
        confirmBtnText = stringResource(R.string.dialog_network_error_retry_btn),
        isBackgroundClickable = false,
        onDismissRequest = navigateBack,
        onConfirmRequest = onRetry,
    )
}

