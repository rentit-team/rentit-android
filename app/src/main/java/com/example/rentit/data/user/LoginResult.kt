package com.example.rentit.data.user

sealed class LoginResult {
    object Idle : LoginResult()
    data class Success(val authCode: String) : LoginResult()
    data class Failure(val message: String) : LoginResult()
}