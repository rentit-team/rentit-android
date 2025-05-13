package com.example.rentit.data.user.model

sealed class GoogleSignInResult {
    data object Idle : GoogleSignInResult()
    data class Success(val authCode: String) : GoogleSignInResult()
    data class Failure(val message: String) : GoogleSignInResult()
}