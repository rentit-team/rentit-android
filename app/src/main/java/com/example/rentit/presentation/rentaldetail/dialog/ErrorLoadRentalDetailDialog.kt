package com.example.rentit.presentation.rentaldetail.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.rentit.R
import com.example.rentit.common.component.dialog.BaseDialog

@Composable
fun ErrorLoadRentalDetailDialog(onDismiss: () -> Unit = {}) {
    BaseDialog(
        title = stringResource(R.string.dialog_rental_detail_load_failed_title),
        content = stringResource(R.string.dialog_rental_detail_load_failed_content),
        confirmBtnText = stringResource(R.string.dialog_rental_detail_load_failed_confirm_btn),
        onDismissRequest = onDismiss,
        onConfirmRequest = onDismiss
    )
}