package com.example.rentit.common.component.calendar

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentit.common.theme.AppBlack
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.theme.RentItTheme
import java.time.LocalDate
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CommonCalendar(yearMonth: YearMonth, disabledDates: List<String> = listOf("2025-04-03", "2025-04-28", "2025-05-01", "2025-05-03"), modifier: Modifier = Modifier) {
    var yearMonth = remember { mutableStateOf(yearMonth) }
    val calModifier = Modifier
        .height(48.dp)
        .fillMaxWidth()

    fun changeMonth(monthsToAdd: Long) { yearMonth.value = yearMonth.value.plusMonths(monthsToAdd) }

    Column(modifier = modifier.padding(bottom = 32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        CalendarHeader(yearMonth.value, { changeMonth(-1) }, { changeMonth(1) })
        DayOfWeek(calModifier)
        UsageDetailDateGrid(yearMonth.value, disabledDates, calModifier)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UsageDetailDateGrid(yearMonth: YearMonth, disabledDates: List<String>, modifier: Modifier) {
    val daysInMonth = yearMonth.lengthOfMonth()
    val firstDayOfWeek = yearMonth.atDay(1).dayOfWeek.value % 7
    val totalSlots = daysInMonth + firstDayOfWeek
    val weeksInMonth = totalSlots / 7 + if(totalSlots % 7 != 0) 1 else 0
    val isTodayInMonth = YearMonth.now().year == yearMonth.year && YearMonth.now().month == yearMonth.month

    val disabledLocalDates = disabledDates.map {LocalDate.parse(it)}
    var disabledDatesInMonth = Array(32) { false }
    disabledLocalDates.forEach {
            disabledDatesInMonth[it.dayOfMonth] = yearMonth.year == it.year && yearMonth.month == it.month
    }

    repeat(weeksInMonth) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            for (i in 0..6) {
                val index = it * 7 + i
                val dayNumber = index - firstDayOfWeek + 1
                val date = if (dayNumber in 1..daysInMonth) yearMonth
                    .atDay(dayNumber) else null
                val isDisabled = date != null && disabledDatesInMonth[dayNumber]
                val isToday = isTodayInMonth && LocalDate.now().dayOfMonth == date?.dayOfMonth
                Box(
                    modifier = Modifier
                        .width(48.dp)
                        .height(48.dp)
                        .padding(4.dp)
                        .clip(CircleShape)
                        .background(if(isDisabled) PrimaryBlue500 else Color.Transparent)
                        .then(
                            if(isToday) Modifier.border(2.dp, PrimaryBlue500, CircleShape)
                            else Modifier
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = date?.dayOfMonth?.toString() ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if(isDisabled) Color.White else AppBlack
                    )
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewUsageDetailBottomDrawer() {
    RentItTheme {
        CommonCalendar(YearMonth.now())
    }
}