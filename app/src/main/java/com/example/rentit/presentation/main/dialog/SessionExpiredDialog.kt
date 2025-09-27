package com.example.rentit.presentation.main.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.rentit.R
import com.example.rentit.common.ui.component.dialog.RentItBaseDialog

@Composable
fun SessionExpiredDialog(navigateBack: () -> Unit, onConfirm: () -> Unit) {
    RentItBaseDialog(
        title = stringResource(R.string.dialog_session_expired_title),
        content = stringResource(R.string.dialog_session_expired_content),
        confirmBtnText = stringResource(R.string.dialog_session_expired_confirm_button),
        isBackgroundClickable = false,
        onDismissRequest = navigateBack,
        onConfirmRequest = onConfirm,
    )
}

