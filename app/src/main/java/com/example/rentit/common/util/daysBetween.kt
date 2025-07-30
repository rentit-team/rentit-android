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
fun daysBetween(startDateStr: String, endDateStr: String): Int {
    val start = parseLocalDateOrNull(startDateStr) ?: return 0
    val end = parseLocalDateOrNull(endDateStr) ?: return 0

    return start.until(end, ChronoUnit.DAYS).toInt() + 1
}

@RequiresApi(Build.VERSION_CODES.O)
fun daysBetween(start: LocalDate?, end: LocalDate?): Int {
    if(start == null || end == null) return 0
    return start.until(end, ChronoUnit.DAYS).toInt().plus(1)
}

