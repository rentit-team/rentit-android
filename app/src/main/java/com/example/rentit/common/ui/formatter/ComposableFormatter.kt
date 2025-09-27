package com.example.rentit.common.ui.formatter

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.rentit.R
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

/**
 * 최소 및 최대 대여 기간을 UI에 표시할 형식의 문자열로 변환
 *
 * @param minPeriod 최소 대여 기간(일)
 * @param maxPeriod 최대 대여 기간(일)
 * @return 포맷팅된 대여 기간 문자열
 */
@Composable
fun formatPeriodText(minPeriod: Int?, maxPeriod: Int?): String {
    return when {
        minPeriod != null && maxPeriod != null -> {
            stringResource(
                R.string.common_rental_period_min_max,
                minPeriod,
                maxPeriod
            )
        }
        minPeriod != null -> {
            stringResource(R.string.common_rental_period_min, minPeriod)
        }
        maxPeriod != null -> {
            stringResource(R.string.common_rental_period_max, maxPeriod)
        }
        else -> stringResource(R.string.common_rental_period_default)
    }
}

/**
 * 최소/최대 레이블을 포함하여 최소 및 최대 대여 기간을 UI에 표시할 형식의 문자열로 변환
 *
 * @param minPeriod 최소 대여 기간(일)
 * @param maxPeriod 최대 대여 기간(일)
 * @return 최소/최대 레이블이 포함된 포맷팅된 문자열
 */
@Composable
fun formatPeriodTextWithLabel(minPeriod: Int?, maxPeriod: Int?): String {
    return when {
        minPeriod != null && maxPeriod != null -> {
            stringResource(
                R.string.common_rental_period_min_max_label,
                minPeriod,
                maxPeriod
            )
        }
        minPeriod != null -> {
            stringResource(R.string.common_rental_period_min_label, minPeriod)
        }
        maxPeriod != null -> {
            stringResource(R.string.common_rental_period_max_label, maxPeriod)
        }
        else -> stringResource(R.string.common_rental_period_default_label)
    }
}

/**
 * `OffsetDateTime`을 현재 시간과 비교하여 상대적인 시간 문자열로 변환
 *
 * - 오늘: "오전/오후 h:mm" 형식 (예: "오후 3:15")
 * - 1~29일 전: "N일 전"
 * - 30일 이상 전: "N달 전"
 *
 * @return 변환된 상대 시간 문자열
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OffsetDateTime.toRelativeDayFormat(): String {
    val now = LocalDate.now()
    val diffDays = Duration.between(this.toLocalDate().atStartOfDay(), now.atStartOfDay()).toDays()

    return when {
        diffDays < 2L -> {
            val formatter = DateTimeFormatter.ofPattern("a h:mm")
            this.format(formatter)
        }
        diffDays in 2..29 -> stringResource(R.string.screen_chat_list_time_days_ago, diffDays)
        else -> stringResource(R.string.screen_chat_list_time_months_ago, diffDays / 30)
    }
}
/**
 * `LocalDateTime`을 현재 시간과 비교하여 상대적인 시간 문자열로 변환 (분/시간 단위 포함)
 *
 * - 1시간 이내: "N분 전"
 * - 오늘: "N시간 전"
 * - 1~29일 전: "N일 전"
 * - 30일 이상 전: "N달 전"
 *
 * @return 변환된 상대 시간 문자열.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LocalDateTime.toRelativeTimeFormat(): String {
    val now = LocalDate.now()
    val diffDays = Duration.between(this.toLocalDate().atStartOfDay(), now.atStartOfDay()).toDays()

    return when {
        diffDays == 0L -> {
            val diffHours = Duration.between(this, OffsetDateTime.now()).toHours()
            if(diffHours == 0L) {
                val diffMinutes = Duration.between(this, OffsetDateTime.now()).toMinutes()
                stringResource(R.string.screen_chat_list_time_minutes_ago, diffMinutes)
            } else {
                stringResource(R.string.screen_chat_list_time_hours_ago, diffHours)
            }
        }
        diffDays in 1..29 -> stringResource(R.string.screen_chat_list_time_days_ago, diffDays)
        diffDays > 29 -> stringResource(R.string.screen_chat_list_time_months_ago, diffDays / 30)
        else -> ""
    }
}