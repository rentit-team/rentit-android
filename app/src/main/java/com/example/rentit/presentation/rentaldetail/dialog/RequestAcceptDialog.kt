package com.example.rentit.presentation.rentaldetail.dialog

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentit.R
import com.example.rentit.common.component.dialog.RentItBaseDialog
import com.example.rentit.common.theme.Gray800
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.common.util.priceFormatter
import com.example.rentit.common.util.rentalPeriodFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RequestAcceptDialog(
    startDate: String,
    endDate: String,
    expectedRevenue: Int,
    onDismiss: () -> Unit,
    onAccept: () -> Unit,
) {
    RentItBaseDialog(
        title = stringResource(R.string.common_dialog_accept_request_title),
        confirmBtnText = stringResource(R.string.common_dialog_accept_request_btn_accept),
        closeBtnText = stringResource(R.string.common_dialog_btn_close),
        onDismissRequest = onDismiss,
        onConfirmRequest = onAccept
    ) {
        Text(
            modifier = Modifier.padding(bottom = 12.dp),
            text = stringResource(R.string.common_dialog_accept_request_label_rental_period),
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = rentalPeriodFormatter(LocalContext.current, startDate,endDate),
            style = MaterialTheme.typography.bodyMedium,
            color = Gray800
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 18.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.common_dialog_accept_request_label_total_price),
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "${priceFormatter(expectedRevenue)} ${stringResource(R.string.common_price_unit)}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
private fun Preview() {
    RentItTheme {
        RequestAcceptDialog(
            "2025-08-17",
            "2025-08-20",
            40000,
            onDismiss = {},
            onAccept = {})
    }
}