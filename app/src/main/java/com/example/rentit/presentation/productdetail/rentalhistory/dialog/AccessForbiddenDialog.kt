package com.example.rentit.presentation.productdetail.rentalhistory.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.rentit.R
import com.example.rentit.common.component.dialog.RentItBaseDialog

@Composable
fun AccessForbiddenDialog(navigateBack: () -> Unit) {
    RentItBaseDialog(
        title = stringResource(R.string.dialog_rental_history_access_forbidden),
        confirmBtnText = stringResource(R.string.common_dialog_btn_close),
        isBackgroundClickable = false,
        onDismissRequest = navigateBack,
        onConfirmRequest = navigateBack,
    )
}