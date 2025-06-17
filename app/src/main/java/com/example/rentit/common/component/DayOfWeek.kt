package com.example.rentit.common.component

fun getKorDayOfWeek(enDayOfWeek: String): String {
    return when(enDayOfWeek){
        "MONDAY" -> "월"
        "TUESDAY" -> "화"
        "WEDNESDAY" -> "수"
        "THURSDAY" -> "목"
        "FRIDAY" -> "금"
        "SATURDAY" -> "토"
        "SUNDAY" -> "일"
        else -> "요일"
    }
}