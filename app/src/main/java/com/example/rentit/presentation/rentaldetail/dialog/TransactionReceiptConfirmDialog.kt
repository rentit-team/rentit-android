package com.example.rentit.presentation.rentaldetail.dialog

import android.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.rentit.R
import com.example.rentit.common.ui.component.dialog.RentItBaseDialog
import com.example.rentit.common.theme.RentItTheme

@Composable
fun TransactionReceiptConfirmDialog(onDismiss: () -> Unit = {}, onConfirmClick: () -> Unit = {}) {
    RentItBaseDialog(
        title = stringResource(R.string.dialog_transaction_receipt_confirm_title),
        content = stringResource(R.string.dialog_transaction_receipt_confirm_content),
        closeBtnText = stringResource(R.string.common_dialog_btn_close),
        confirmBtnText = stringResource(R.string.dialog_transaction_receipt_confirm_btn),
        onDismissRequest = onDismiss,
        onConfirmRequest = onConfirmClick
    )
}

@Preview(showBackground = true, backgroundColor = Color.DKGRAY.toLong())
@Composable
fun TransactionReceiptConfirmDialogPreview() {
    RentItTheme {
        TransactionReceiptConfirmDialog()
    }
}