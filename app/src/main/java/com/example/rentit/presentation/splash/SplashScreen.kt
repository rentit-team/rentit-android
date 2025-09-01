package com.example.rentit.presentation.splash

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.rentit.R
import com.example.rentit.common.component.dialog.BaseDialog
import com.example.rentit.common.component.layout.LoadingScreen

@Composable
fun SplashScreen(
    isLoading: Boolean,
    showErrorDialog: Boolean,
    onRetry: () -> Unit,
) {
    if(showErrorDialog) {
        BaseDialog(
            title = stringResource(R.string.dialog_server_connect_error_title),
            content = stringResource(R.string.dialog_server_connect_error_content),
            confirmBtnText = stringResource(R.string.dialog_server_connect_error_retry_btn),
            isBackgroundClickable = false,
            onDismissRequest = onRetry,
            onConfirmRequest = onRetry,
        )
    }

    LoadingScreen(isLoading)
}