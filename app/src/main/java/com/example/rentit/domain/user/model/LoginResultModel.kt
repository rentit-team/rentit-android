package com.example.rentit.domain.user.model

data class LoginResultModel(
    val isRegistered: Boolean,
    val userNickname: String,
    val userName: String,
    val userEmail: String
)