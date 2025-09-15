package com.example.rentit.data.auth.dto

import com.google.gson.annotations.SerializedName

data class GoogleLoginRequestDto(
    @SerializedName("code")
    val code: String,

    @SerializedName("redirectUri")
    val redirectUri: String
)