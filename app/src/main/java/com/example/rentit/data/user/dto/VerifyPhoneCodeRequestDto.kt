package com.example.rentit.data.user.dto

import com.google.gson.annotations.SerializedName

data class VerifyPhoneCodeRequestDto(
    @SerializedName("phoneNumber")
    val phoneNumber: String,

    @SerializedName("code")
    val code: String,
)