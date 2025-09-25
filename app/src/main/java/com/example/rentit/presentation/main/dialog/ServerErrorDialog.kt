package com.example.rentit.presentation.main.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.rentit.R
import com.example.rentit.common.ui.component.dialog.RentItBaseDialog

@Composable
fun ServerErrorDialog(navigateBack: () -> Unit, onRetry: () -> Unit) {
    RentItBaseDialog(
        title = stringResource(R.string.dialog_server_error_title),
        content = stringResource(R.string.dialog_server_error_content),
        confirmBtnText = stringResource(R.string.dialog_server_error_retry_btn),
        isBackgroundClickable = false,
        onDismissRequest = navigateBack,
        onConfirmRequest = onRetry,
    )
}