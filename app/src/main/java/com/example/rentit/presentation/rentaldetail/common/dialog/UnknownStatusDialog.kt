package com.example.rentit.presentation.rentaldetail.common.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.rentit.R
import com.example.rentit.common.component.BaseDialog

@Composable
fun UnknownStatusDialog(onDismiss: () -> Unit = {}) {
    BaseDialog(
        titleText = stringResource(R.string.screen_rental_detail_unknown_dialog_title),
        confirmText = stringResource(R.string.screen_rental_detail_unknown_dialog_confirm_btn),
        onCloseRequest = onDismiss,
        onConfirmRequest = onDismiss
    )
}