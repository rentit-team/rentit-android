package com.example.rentit.presentation.auth.join.nickname

data class JoinNicknameState(
    val nickname: String = "",
    val showNicknameBlankError: Boolean = false,
    val buttonEnabled: Boolean = true,
    val isLoading: Boolean = false
)