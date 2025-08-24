package com.example.rentit.presentation.auth.join.phoneverification.model

import com.example.rentit.R

object VerificationErrorCode {
    const val CODE_NOT_FOUND_OR_EXPIRED = "Verification code not found or expired"
    const val INCORRECT_CODE = "Incorrect verification code"
    const val VERIFICATION_FAILED = "Verification failed"
}

sealed class VerificationError {
    data object CodeNotFoundOrExpired : VerificationError()
    data object IncorrectCode : VerificationError()
    data object VerificationFailed : VerificationError()
    data object Failed: VerificationError()

    companion object {
        fun from(message: String?): VerificationError =
            when (message) {
                VerificationErrorCode.CODE_NOT_FOUND_OR_EXPIRED -> CodeNotFoundOrExpired
                VerificationErrorCode.INCORRECT_CODE -> IncorrectCode
                VerificationErrorCode.VERIFICATION_FAILED -> VerificationFailed
                else -> Failed
            }
    }
}

fun VerificationError.toUiMessage(): Int =
    when (this) {
        VerificationError.CodeNotFoundOrExpired -> R.string.error_code_expired
        VerificationError.IncorrectCode -> R.string.error_incorrect_code
        VerificationError.VerificationFailed -> R.string.error_request_not_found
        VerificationError.Failed -> R.string.error_verification_fail
    }