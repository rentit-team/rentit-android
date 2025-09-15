package com.example.rentit.data.user.dto

import com.google.gson.annotations.SerializedName

data class RefreshAccessTokenRequestDto(
    @SerializedName("refreshToken")
    val refreshToken: String
)