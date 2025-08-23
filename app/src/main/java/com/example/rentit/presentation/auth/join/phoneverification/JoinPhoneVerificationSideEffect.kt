package com.example.rentit.presentation.auth.join.phoneverification

sealed class JoinPhoneVerificationSideEffect {
    data object NavigateBack : JoinPhoneVerificationSideEffect()
    data object ToastSendCodeFailed : JoinPhoneVerificationSideEffect()
    data object ToastTooManyRequests : JoinPhoneVerificationSideEffect()
    data object ToastVerificationSuccess : JoinPhoneVerificationSideEffect()
    data object ToastRequestNotFound : JoinPhoneVerificationSideEffect()
}