package com.example.rentit.presentation.pay

sealed class PaySideEffect {
    data object ToastPayFailed : PaySideEffect()
    data object NavigateBackToRentalDetail : PaySideEffect()
}