package com.example.rentit.presentation.rentaldetail.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.rentit.R
import com.example.rentit.common.component.dialog.BaseDialog

@Composable
fun RentalCancelDialog(onClose: () -> Unit, onCancelAccept: () -> Unit) {
    BaseDialog(
        title = stringResource(R.string.dialog_rental_detail_rental_cancel_title),
        content = stringResource(R.string.dialog_rental_detail_rental_cancel_content),
        confirmBtnText = stringResource(R.string.dialog_rental_detail_rental_cancel_btn_confirm),
        closeBtnText = stringResource(R.string.common_dialog_btn_close),
        onCloseRequest = onClose,
        onConfirmRequest = onCancelAccept
    )
}