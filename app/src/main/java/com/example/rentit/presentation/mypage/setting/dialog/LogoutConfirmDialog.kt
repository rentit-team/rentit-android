package com.example.rentit.presentation.mypage.setting.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.rentit.R
import com.example.rentit.common.component.dialog.BaseDialog

@Composable
fun LogoutConfirmDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    BaseDialog(
        title = stringResource(R.string.dialog_logout_confirm_title),
        content = stringResource(R.string.dialog_logout_confirm_content),
        confirmBtnText = stringResource(R.string.dialog_logout_confirm_btn_confirm),
        closeBtnText = stringResource(R.string.common_dialog_btn_close),
        onDismissRequest = onDismiss,
        onConfirmRequest = onConfirm
    )
}