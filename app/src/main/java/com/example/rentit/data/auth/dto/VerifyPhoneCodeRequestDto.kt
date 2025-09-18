package com.example.rentit.data.auth.dto

import com.google.gson.annotations.SerializedName

data class VerifyPhoneCodeRequestDto(
    private val rawPhoneNumber: String,

    @SerializedName("code")
    val code: String,
) {
    @SerializedName("phoneNumber")
    val phoneNumber: String = rawPhoneNumber.filter { it.isDigit() }
}