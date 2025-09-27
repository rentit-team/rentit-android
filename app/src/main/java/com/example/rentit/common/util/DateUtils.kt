package com.example.rentit.common.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

/**
 * 시작일과 종료일을 **포함한** 총 일수를 계산합니다.
 * 날짜 객체 중 하나라도 null이면 0을 반환합니다.
 *
 * 예: `LocalDate.of(2025, 8, 17)` ~ `LocalDate.of(2025, 8, 20)` → 4
 * @return 두 날짜 사이의 총 일수.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun inclusiveDaysBetween(start: LocalDate?, end: LocalDate?): Int {
    if(start == null || end == null) return 0
    return start.until(end, ChronoUnit.DAYS).toInt().plus(1)
}

/**
 * 오늘 날짜로부터 특정 날짜까지 남은 일수를 계산 (D-day)
 * 날짜 파싱에 실패하면 0을 반환
 *
 * @return 오늘로부터 목표 날짜까지 남은 일수
 */
@RequiresApi(Build.VERSION_CODES.O)
fun daysFromToday(targetDateStr: String): Int {
    val today = LocalDate.now()
    val targetDate = parseLocalDateOrNull(targetDateStr) ?: return 0
    return ChronoUnit.DAYS.between(today, targetDate).toInt()
}

@RequiresApi(Build.VERSION_CODES.O)
fun daysFromToday(targetDate: LocalDate?): Int {
    val today = LocalDate.now()
    return targetDate?.let {ChronoUnit.DAYS.between(today, targetDate).toInt() } ?: 0
}

/**
 * 날짜 형식의 문자열을 `LocalDate` 객체로 안전하게 변환
 * 파싱에 실패할 경우 null을 반환
 *
 * @param dateStr 변환할 날짜 문자열 (yyyy-MM-dd 형식)
 * @return 변환된 `LocalDate`/'LocalDateTime' 객체 또는 null
 */
@RequiresApi(Build.VERSION_CODES.O)
fun parseLocalDateOrNull(dateStr: String): LocalDate? {
    return try {
        LocalDate.parse(dateStr)
    } catch (e: Exception) {
        null
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun parseLocalDateTimeOrNull(dateStr: String): LocalDateTime? {
    return try {
        LocalDateTime.parse(dateStr)
    } catch (e: Exception) {
        null
    }
}

