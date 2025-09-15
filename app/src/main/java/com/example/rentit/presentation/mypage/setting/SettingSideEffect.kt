package com.example.rentit.presentation.mypage.setting

sealed class SettingSideEffect {
    data object NavigateToLogin: SettingSideEffect()
    data object ToastLogoutComplete: SettingSideEffect()
}