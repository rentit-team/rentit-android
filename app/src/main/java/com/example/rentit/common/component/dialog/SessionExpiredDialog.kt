package com.example.rentit.common.component.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.rentit.R

@Composable
fun SessionExpiredDialog(navigateBack: () -> Unit, onConfirm: () -> Unit) {
    BaseDialog(
        title = stringResource(R.string.dialog_session_expired_title),
        content = stringResource(R.string.dialog_session_expired_content),
        confirmBtnText = stringResource(R.string.dialog_session_expired_confirm_button),
        isBackgroundClickable = false,
        onDismissRequest = navigateBack,
        onConfirmRequest = onConfirm,
    )
}

