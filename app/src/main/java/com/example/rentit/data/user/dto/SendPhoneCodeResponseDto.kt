package com.example.rentit.data.user.dto

import com.google.gson.annotations.SerializedName

data class SendPhoneCodeResponseDto(
    @SerializedName("message")
    val message: String,

    @SerializedName("expiresIn")
    val expiresIn: Int,
)