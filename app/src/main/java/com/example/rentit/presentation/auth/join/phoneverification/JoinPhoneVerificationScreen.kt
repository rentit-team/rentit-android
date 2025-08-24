package com.example.rentit.presentation.auth.join.phoneverification

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentit.R
import com.example.rentit.common.component.CommonButton
import com.example.rentit.common.component.CommonTextField
import com.example.rentit.common.component.CommonTopAppBar
import com.example.rentit.common.component.paddingForBottomBarButton
import com.example.rentit.common.component.screenHorizontalPadding
import com.example.rentit.common.theme.PretendardTextStyle
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.common.theme.White
import com.example.rentit.presentation.auth.join.components.InputErrorMessage

const val PHONE_NUMBER_DIGITS_LENGTH = 11
const val PHONE_NUMBER_SECOND_SPLIT_MIN = 8
const val PHONE_NUMBER_FIRST_SPLIT_MIN = 4

@Composable
fun JoinPhoneVerificationScreen(
    phoneNumber: String,
    code: String,
    remainingMinutes: Int,
    remainingSeconds: Int,
    @StringRes errorMessageRes: Int?,
    isRequestEnabled: Boolean,
    isConfirmEnabled: Boolean,
    showRemainingTime: Boolean,
    onBackPressed: () -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    onCodeChange: (String) -> Unit,
    onRequestCode: () -> Unit,
    onConfirm: () -> Unit,
    ) {
    Scaffold(
        topBar = { CommonTopAppBar(title = stringResource(R.string.screen_join_title)) { onBackPressed() } },
        bottomBar = { CommonButton(
            modifier = Modifier
                .screenHorizontalPadding()
                .paddingForBottomBarButton(),
            text = stringResource(R.string.screen_join_phone_verification_btn_check_code),
            enabled = isConfirmEnabled,
            containerColor = PrimaryBlue500,
            contentColor = White
        ) { onConfirm() } }
    ) {
        Column(Modifier
            .padding(it)
            .screenHorizontalPadding()) {

            Spacer(Modifier.weight(0.8f))

            PhoneVerificationGuideText()

            PhoneVerificationForm(
                phoneNumber = phoneNumber,
                code = code,
                isRequestEnabled = isRequestEnabled,
                onPhoneNumberChange = onPhoneNumberChange,
                onCodeChange = onCodeChange,
                onRequestCode = onRequestCode
            )

            if(showRemainingTime) {
                InputErrorMessage(
                    stringResource(
                        R.string.screen_join_phone_verification_text_remaining_time,
                        remainingMinutes,
                        remainingSeconds
                    )
                )
            }

            errorMessageRes?.let {
                InputErrorMessage(stringResource(errorMessageRes))
            }

            Spacer(Modifier.weight(2f))
        }
    }
}

@Composable
private fun PhoneVerificationGuideText() {
    Text(
        modifier = Modifier.padding(bottom = 8.dp),
        text = stringResource(R.string.screen_join_phone_verification_title),
        style = PretendardTextStyle.headline_bold,
    )
    Text(
        modifier = Modifier.padding(bottom = 27.dp),
        text = stringResource(R.string.screen_join_phone_verification_guide),
        style = MaterialTheme.typography.labelMedium,
    )
}

@Composable
private fun PhoneVerificationForm(
    phoneNumber: String,
    code: String,
    isRequestEnabled: Boolean = true,
    onPhoneNumberChange: (String) -> Unit = {},
    onCodeChange: (String) -> Unit = {},
    onRequestCode: () -> Unit = {},
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
        CommonTextField(
            modifier = Modifier.weight(1f),
            value = TextFieldValue(phoneNumber, TextRange(phoneNumber.length)), // 맨 뒤로 커서 조정
            onValueChange = {
                val formatted = formatPhoneNumber(it.text)
                onPhoneNumberChange(formatted)
            },
            placeholder = stringResource(R.string.screen_join_phone_verification_placeholder_phone_number),
            keyboardType = KeyboardType.Number,
        )
        Button(
            onClick = onRequestCode,
            enabled = isRequestEnabled,
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue500),
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 14.dp)
        ) {
            Text(
                text = stringResource(R.string.screen_join_phone_verification_btn_request_code),
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }
    CommonTextField(
        modifier = Modifier.padding(top = 10.dp),
        value = code,
        onValueChange = onCodeChange,
        placeholder = stringResource(R.string.screen_join_phone_verification_placeholder_code),
        keyboardType = KeyboardType.Number,
    )
}


private fun formatPhoneNumber(phoneNumber: String): String {
    val digits = phoneNumber.filter { c -> c.isDigit() }.take(PHONE_NUMBER_DIGITS_LENGTH)
    return when (digits.length) {
        in PHONE_NUMBER_SECOND_SPLIT_MIN..PHONE_NUMBER_DIGITS_LENGTH -> digits.replaceFirst(
            Regex("(\\d{3})(\\d{4})(\\d+)"),
            "$1-$2-$3"
        )

        in PHONE_NUMBER_FIRST_SPLIT_MIN..<PHONE_NUMBER_SECOND_SPLIT_MIN -> digits.replaceFirst(
            Regex("(\\d{3})(\\d+)"),
            "$1-$2"
        )

        else -> digits
    }
}

@Preview
@Composable
fun JoinPhoneVerificationScreenPreview() {
    RentItTheme {
        JoinPhoneVerificationScreen(
            phoneNumber = "",
            code = "",
            remainingMinutes = 0,
            remainingSeconds = 0,
            errorMessageRes = R.string.error_verification_fail,
            isRequestEnabled = true,
            isConfirmEnabled = true,
            showRemainingTime = true,
            onPhoneNumberChange = {},
            onCodeChange = {},
            onRequestCode = {},
            onBackPressed = {},
            onConfirm = {},
        )
    }
}
