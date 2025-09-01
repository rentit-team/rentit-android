package com.example.rentit.presentation.rentaldetail.dialog

import android.graphics.Color
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.rentit.R
import com.example.rentit.common.component.dialog.BaseDialog
import com.example.rentit.common.theme.RentItTheme

@Composable
fun PhotoLoadFailedDialog(onDismiss: () -> Unit = {}) {
    BaseDialog(
        title = stringResource(R.string.dialog_fetch_photos_failed_title),
        confirmBtnText = stringResource(R.string.dialog_rental_detail_unknown_confirm_btn),
        onDismissRequest = onDismiss,
        onConfirmRequest = onDismiss
    ) {
        Text(
            text = stringResource(R.string.dialog_fetch_photos_failed_content),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Preview(showBackground = true, backgroundColor = Color.DKGRAY.toLong())
@Composable
fun PhotoLoadFailedDialogPreview() {
    RentItTheme {
        PhotoLoadFailedDialog()
    }
}