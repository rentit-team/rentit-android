package com.example.rentit.presentation.auth.join.phoneverification

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.rentit.common.theme.RentItTheme

private const val PHONE_NUMBER_TOTAL_LENGTH = 13

@Composable
fun JoinPhoneVerificationRoute() {
    var phoneNumber by remember { mutableStateOf("") }
    var code by remember { mutableStateOf("") }
    var showCodeError by remember { mutableStateOf(false) }
    var isCodeFieldEnabled by remember { mutableStateOf(true) }

    val isRequestEnabled by remember(phoneNumber) { mutableStateOf(phoneNumber.length == PHONE_NUMBER_TOTAL_LENGTH) }
    val isConfirmEnabled by remember(code) { mutableStateOf(code.isNotBlank()) }

    RentItTheme {
        JoinPhoneVerificationScreen(
            phoneNumber = phoneNumber,
            code = code,
            isRequestEnabled = isRequestEnabled,
            isCodeFieldEnabled = isCodeFieldEnabled,
            isConfirmEnabled = isConfirmEnabled,
            showCodeError = showCodeError,
            onPhoneNumberChange = { phoneNumber = it },
            onCodeChange = {
                code = it
                showCodeError = false
            },
            onRequestCode = { },
            onBackPressed = { },
            onConfirm = {
                if (code.isBlank()) showCodeError = true
            },
        )
    }
}