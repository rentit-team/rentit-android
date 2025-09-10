package com.example.rentit.data.product.dto

import com.google.gson.annotations.SerializedName

data class ChatAccessibilityResponseDto(
    @SerializedName("canAccessChat")
    val canAccessChat: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("chatroomId")
    val chatroomId: String,
)