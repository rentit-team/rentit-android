package com.example.rentit.presentation.auth.join.phoneverification

private const val PHONE_NUMBER_TOTAL_LENGTH = 13

data class JoinPhoneVerificationState(
    val phoneNumber: String = "",
    val code: String = "",
    val showCodeError: Boolean = false,
    val isCodeFieldEnabled: Boolean = false,
) {
    val isRequestEnabled: Boolean
        get() = phoneNumber.length == PHONE_NUMBER_TOTAL_LENGTH
    val isConfirmEnabled: Boolean
        get() = code.isNotBlank()
}