package com.example.rentit.common.util

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.rentit.R
import java.text.NumberFormat
import java.time.LocalDate

/**
 * 숫자로 된 가격을 세 자리마다 쉼표(,)가 포함된 문자열로 변환
 *
 * 예: 10000 -> "10,000"
 * @return 쉼표가 포함된 가격 문자열
 */
fun priceFormatter(price: Int): String = NumberFormat.getNumberInstance().format(price)

/**
 * 주어진 시작일과 종료일을 'yy.MM.dd (요일) ~ yy.MM.dd (요일) · N일' 형식의 문자열로 변환
 * 날짜 파싱에 실패할 경우 에러 메시지를 반환
 *
 * 예: "2025.08.17" ~ "2025.08.20" → "25.08.17 (일) ~ 25.08.20 (수) · 4일"
 * @return 형식에 맞게 변환된 대여 기간 문자열 또는 에러 메시지
 */
@RequiresApi(Build.VERSION_CODES.O)
fun rentalPeriodFormatter(context: Context, startDateStr: String, endDateStr: String): String {
    val errorMsg = context.getString(R.string.util_error_rental_period_unavailable)
    val start = parseLocalDateOrNull(startDateStr) ?: return errorMsg
    val end = parseLocalDateOrNull(endDateStr) ?: return errorMsg

    val startDayOfWeekKor = start.dayOfWeek.getKorLabel()
    val endDayOfWeekKor = end.dayOfWeek.getKorLabel()
    val period = inclusiveDaysBetween(start, end)

    return context.getString(
        R.string.util_formatted_period,
        start.toShortFormat(),
        startDayOfWeekKor,
        end.toShortFormat(),
        endDayOfWeekKor,
        period
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun rentalPeriodFormatter(context: Context, startDate: LocalDate?, endDate: LocalDate?): String {
    if(startDate == null || endDate == null) return context.getString(R.string.util_error_rental_period_unavailable)

    val startDayOfWeekKor = startDate.dayOfWeek.getKorLabel()
    val endDayOfWeekKor = endDate.dayOfWeek.getKorLabel()
    val period = inclusiveDaysBetween(startDate, endDate)

    return context.getString(
        R.string.util_formatted_period,
        startDate.toShortFormat(),
        startDayOfWeekKor,
        endDate.toShortFormat(),
        endDayOfWeekKor,
        period
    )
}