package com.example.rentit.domain.auth.model

data class LoginResultModel(
    val isRegistered: Boolean,
    val userNickname: String,
    val userName: String,
    val userEmail: String
)