package com.example.rentit.presentation.pay

sealed class PaySideEffect {
    data object ToastPayFailed : PaySideEffect()
    data object NavigateBack : PaySideEffect()
    data class CommonError(val throwable: Throwable): PaySideEffect()
}