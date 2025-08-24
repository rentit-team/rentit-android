package com.example.rentit.presentation.auth.join.phoneverification

private const val PHONE_NUMBER_TOTAL_LENGTH = 13

data class JoinPhoneVerificationState(
    val phoneNumber: String = "",
    val code: String = "",
    val errorMessageRes: Int? = null,
    private val remainingTime: Int? = null,
) {
    val isRequestEnabled: Boolean
        get() = phoneNumber.length == PHONE_NUMBER_TOTAL_LENGTH

    val isConfirmEnabled: Boolean
        get() = code.isNotBlank()

    val showRemainingTime: Boolean
        get() = remainingTime != null

    val remainingMinutes: Int
        get() = remainingTime?.div(60) ?: 0

    val remainingSeconds: Int
        get() = remainingTime?.rem(60) ?: 0
}