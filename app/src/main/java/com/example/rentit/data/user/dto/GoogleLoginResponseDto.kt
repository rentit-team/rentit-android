package com.example.rentit.data.user.dto

import com.google.gson.annotations.SerializedName

data class GoogleLoginResponseDto(
    @SerializedName("data")
    val data: LoginDataDto
)

data class LoginDataDto(
    @SerializedName("accessToken")
    val accessToken: TokenDto,

    @SerializedName("refreshToken")
    val refreshToken: TokenDto,

    @SerializedName("user")
    val user: UserDto,

    @SerializedName("isUserRegistered")
    val isUserRegistered: Boolean
)

data class TokenDto(
    @SerializedName("data")
    val token: String,

    @SerializedName("meta")
    val meta: TokenMetaDto
)

data class TokenMetaDto(
    @SerializedName("expiresAt")
    val expiresAt: Long
)

data class UserDto(
    @SerializedName("email")
    val email: String,

    @SerializedName("name")
    val name: String
)
