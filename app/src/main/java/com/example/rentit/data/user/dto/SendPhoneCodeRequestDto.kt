package com.example.rentit.data.user.dto

import com.google.gson.annotations.SerializedName

data class SendPhoneCodeRequestDto(
    private val rawPhoneNumber: String,
) {
    @SerializedName("phoneNumber")
    val phoneNumber: String = rawPhoneNumber.filter { it.isDigit() }
}