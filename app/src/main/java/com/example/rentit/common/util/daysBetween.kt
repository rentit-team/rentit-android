package com.example.rentit.common.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.temporal.ChronoUnit

/**
 * 주어진 시작일과 종료일 사이의 일수를 계산
 *
 * 예: "2025.08.17" ~ "2025.08.20" → 4일
 */

@RequiresApi(Build.VERSION_CODES.O)
fun inclusiveDaysBetween(start: LocalDate?, end: LocalDate?): Int {
    if(start == null || end == null) return 0
    return start.until(end, ChronoUnit.DAYS).toInt().plus(1)
}

/**
 * 주어진 날짜와 오늘 날짜 사이의 일수를 계산
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


