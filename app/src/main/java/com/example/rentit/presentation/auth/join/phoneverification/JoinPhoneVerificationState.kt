package com.example.rentit.presentation.auth.join.phoneverification

private const val PHONE_NUMBER_TOTAL_LENGTH = 13

data class JoinPhoneVerificationState(
    val phoneNumber: String = "",
    val code: String = "",
    private val remainingTime: Int = 0,
    val showRemainingTime: Boolean = false,
    val showCodeError: Boolean = false,
) {
    val isRequestEnabled: Boolean
        get() = phoneNumber.length == PHONE_NUMBER_TOTAL_LENGTH
    val isConfirmEnabled: Boolean
        get() = code.isNotBlank()
    val remainingMinutes get() = remainingTime / 60
    val remainingSeconds get() = remainingTime % 60
}