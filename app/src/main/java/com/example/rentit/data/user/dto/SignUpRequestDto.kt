package com.example.rentit.data.user.dto

import com.google.gson.annotations.SerializedName

data class SignUpRequestDto(
    @SerializedName("name")
    val name: String?,

    @SerializedName("email")
    val email: String,

    @SerializedName("nickname")
    val nickname: String,

    @SerializedName("profileImgUrl")
    val profileImgUrl: String?,
)