package com.example.rentit.data.user.model

sealed class LoginResult {
    data object Idle : LoginResult()
    data class Success(val authCode: String) : LoginResult()
    data class Failure(val message: String) : LoginResult()
}