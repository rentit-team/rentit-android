package com.example.rentit.data.auth.dto

import com.google.gson.annotations.SerializedName

data class VerifyPhoneCodeResponseDto(
    @SerializedName("message")
    val message: String,

    @SerializedName("verified")
    val verified: Boolean
)