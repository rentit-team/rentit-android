package com.example.rentit.navigation.auth

import kotlinx.serialization.Serializable

sealed class AuthRoute {
    @Serializable
    data object Login : AuthRoute()

    @Serializable
    data class JoinPhoneVerification(val name: String?, val email: String?) : AuthRoute()

    @Serializable
    data class JoinNickname(val name: String?, val email: String?) : AuthRoute()
}