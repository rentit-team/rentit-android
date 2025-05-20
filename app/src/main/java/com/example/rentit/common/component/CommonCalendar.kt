package com.example.rentit.common.component

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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentit.R
import com.example.rentit.common.theme.AppBlack
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.theme.RentItTheme
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CommonCalendar(yearMonth: YearMonth, disabledDates: List<String> = listOf("2025-04-03", "2025-04-28", "2025-05-01", "2025-05-02")) {
    var yearMonth = remember { mutableStateOf(yearMonth) }
    val modifier = Modifier
        .height(48.dp)
        .fillMaxWidth()
        .padding(horizontal = 12.dp)

    fun changeMonth(monthsToAdd: Long) { yearMonth.value = yearMonth.value.plusMonths(monthsToAdd) }

    Column(modifier = Modifier.padding(bottom = 32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        CalendarHeader(yearMonth.value, { changeMonth(-1) }, { changeMonth(1) })
        DayOfWeek(modifier)
        Dates(yearMonth.value, disabledDates, modifier)
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarHeader(yearMonth: YearMonth, leftChevronOnClick: () -> Unit, rightChevronOnClick: () -> Unit) {
    Row(modifier = Modifier.width(336.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Text(modifier = Modifier.weight(1F), text = "${yearMonth.year} ${yearMonth.month.value}월", style = MaterialTheme.typography.bodyLarge)
        IconButton(onClick = leftChevronOnClick) {
            Icon(modifier = Modifier.width(7.dp), painter = painterResource(id = R.drawable.ic_chevron_left), contentDescription = stringResource(
                id = R.string.common_calendar_left_chevron_description) )
        }
        IconButton(onClick = rightChevronOnClick) {
            Icon(modifier = Modifier.width(7.dp), painter = painterResource(id = R.drawable.ic_chevron_right), contentDescription = stringResource(
                id = R.string.common_calendar_right_chevron_description) )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DayOfWeek(modifier: Modifier) {
    Row(modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center) {
        DayOfWeek.entries.forEach {
            Text(modifier = Modifier.width(48.dp), text = it.name.take(1), style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Dates(yearMonth: YearMonth, disabledDates: List<String>, modifier: Modifier) {
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