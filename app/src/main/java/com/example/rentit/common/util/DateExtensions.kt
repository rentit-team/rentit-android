package com.example.rentit.common.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val korLabels = listOf("월", "화", "수", "목", "금", "토", "일")

/**
 * `DayOfWeek` Enum 값을 한글 요일 문자열로 변환
 *
 * 예: `DayOfWeek.MONDAY` -> "월"
 * @return 한글 요일 한 글자 (예: "월", "화").
 */
@RequiresApi(Build.VERSION_CODES.O)
fun DayOfWeek.getKorLabel(): String = korLabels[this.ordinal]

/**
 * `LocalDate` 객체를 'yy.MM.dd' 형식의 짧은 문자열로 변환
 *
 * 예: `LocalDate.of(2025, 9, 26)` -> "25.09.26"
 * @return 'yy.MM.dd' 형식의 날짜 문자열.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.toShortFormat(): String =
    this.format(DateTimeFormatter.ofPattern("yy.MM.dd"))
