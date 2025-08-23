package com.example.rentit.data.user.dto

import com.google.gson.annotations.SerializedName

data class SendPhoneCodeResponseDto(
    @SerializedName("message")
    private val message: String,

    @SerializedName("expiresIn")
    private val expiresIn: Int,
)