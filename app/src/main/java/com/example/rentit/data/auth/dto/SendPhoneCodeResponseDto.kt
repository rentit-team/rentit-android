package com.example.rentit.data.auth.dto

import com.google.gson.annotations.SerializedName

data class SendPhoneCodeResponseDto(
    @SerializedName("message")
    val message: String,

    @SerializedName("expiresIn")
    val expiresIn: Int,
)