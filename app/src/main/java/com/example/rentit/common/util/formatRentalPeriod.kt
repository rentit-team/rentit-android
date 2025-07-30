package com.example.rentit.common.util

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.rentit.R

/**
 * 주어진 시작일과 종료일을 'yy.MM.dd (요일) ~ yy.MM.dd (요일) · N일' 형식의 문자열로 변환
 *
 * 예: "2025.08.17" ~ "2025.08.20" → "25.08.17 (일) ~ 25.08.20 (수) · 4일"
 */

@RequiresApi(Build.VERSION_CODES.O)
fun formatRentalPeriod(context: Context, startDateStr: String, endDateStr: String): String {
    val errorMsg = context.getString(R.string.util_error_rental_period_unavailable)
    val start = parseLocalDateOrNull(startDateStr) ?: return errorMsg
    val end = parseLocalDateOrNull(endDateStr) ?: return errorMsg

    val startDayOfWeekKor = start.dayOfWeek.getKorLabel()
    val endDayOfWeekKor = end.dayOfWeek.getKorLabel()
    val period = daysBetween(start, end)

    return context.getString(
        R.string.util_formatted_period,
        start.toShortFormat(),
        startDayOfWeekKor,
        end.toShortFormat(),
        endDayOfWeekKor,
        period
    )
}