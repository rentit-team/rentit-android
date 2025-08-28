package com.example.rentit.presentation.auth.login

sealed class LoginSideEffect {
    data class NavigateToJoin(val email: String, val name: String) : LoginSideEffect()
    data object NavigateToHome : LoginSideEffect()
    data class ToastGreetingMessage(val name: String): LoginSideEffect()
    data object ToastGoogleSignInFailed: LoginSideEffect()
    data object ToastGoogleSignInError: LoginSideEffect()
    data object ToastLoginFailed: LoginSideEffect()
    data object ToastLoginServerError: LoginSideEffect()
}