package com.example.rentit.data.user.dto

import com.google.gson.annotations.SerializedName

data class MyInfoResponseDto(
    @SerializedName("data")
    val data: MyInfoDto,
)

data class MyInfoDto(
    @SerializedName("userId")
    val userId: Long,

    @SerializedName("nickname")
    val nickname: String,

    @SerializedName("name")
    val name: String?,

    @SerializedName("email")
    val email: String,

    @SerializedName("profileImgUrl")
    val profileImgUrl: String?,

    @SerializedName("region")
    val region: List<String>?,
)