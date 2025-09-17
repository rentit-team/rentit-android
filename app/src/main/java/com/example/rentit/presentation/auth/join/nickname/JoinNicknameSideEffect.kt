package com.example.rentit.presentation.auth.join.nickname

sealed class JoinNicknameSideEffect {
    data object NavigateToHome : JoinNicknameSideEffect()
    data object JoinNicknameSuccess : JoinNicknameSideEffect()
    data object JoinNicknameError : JoinNicknameSideEffect()
}