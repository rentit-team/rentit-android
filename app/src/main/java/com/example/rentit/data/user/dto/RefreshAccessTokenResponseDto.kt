package com.example.rentit.data.user.dto

import com.google.gson.annotations.SerializedName

data class RefreshAccessTokenResponseDto(
    @SerializedName("data")
    val data: AccessTokenDataDto
)

data class AccessTokenDataDto(
    @SerializedName("accessToken")
    val accessToken: AccessTokenDto
)

data class AccessTokenDto(
    @SerializedName("data")
    val data: String
)

