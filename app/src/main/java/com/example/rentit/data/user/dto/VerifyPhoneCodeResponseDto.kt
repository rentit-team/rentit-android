package com.example.rentit.data.user.dto

import com.google.gson.annotations.SerializedName

data class VerifyPhoneCodeResponseDto(
    @SerializedName("message")
    val message: String,

    @SerializedName("error")
    val error: String,

    @SerializedName("verified")
    val verified: Boolean
)