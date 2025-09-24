package com.example.rentit.common.component.formatter

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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OffsetDateTime.toRelativeDayFormat(): String {
    val now = LocalDate.now()
    val diffDays = Duration.between(this.toLocalDate().atStartOfDay(), now.atStartOfDay()).toDays()

    return when {
        diffDays == 0L -> {
            val formatter = DateTimeFormatter.ofPattern("a h:mm")
            this.format(formatter)
        }
        diffDays in 1..29 -> stringResource(R.string.screen_chat_list_time_days_ago, diffDays)
        diffDays > 29 -> stringResource(R.string.screen_chat_list_time_months_ago, diffDays / 30)
        else -> ""
    }
}

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