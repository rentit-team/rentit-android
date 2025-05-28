package com.example.rentit.feature.product.component.calendar

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentit.common.component.basicRoundedGrayBorder
import com.example.rentit.common.component.screenHorizontalPadding
import com.example.rentit.common.theme.AppBlack
import com.example.rentit.common.theme.Gray200
import com.example.rentit.common.theme.PrimaryBlue300
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.common.theme.SecondaryYellow
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateRangePicker(yearMonth: YearMonth = YearMonth.now(), disabledDates: List<String> = listOf("2025-04-03", "2025-04-28", "2025-05-01", "2025-05-03"), modifier: Modifier = Modifier) {
    var yearMonth = remember { mutableStateOf(yearMonth) }
    val cellWidth = 48.dp

    // 시작일 설정, 종료일 설정 모드 확인
    var isSelectingStartDate by remember { mutableStateOf(false) }
    var isSelectingEndDate by remember { mutableStateOf(false) }

    var rentalStartDate: LocalDate? by remember { mutableStateOf(null) }
    var rentalEndDate: LocalDate? by remember { mutableStateOf(null) }
    var rentalPeriod: Int =
        if (rentalStartDate != null && rentalEndDate != null) ChronoUnit.DAYS.between(
            rentalStartDate,
            rentalEndDate
        ).toInt() + 1 else 0

    fun changeMonth(monthsToAdd: Long) { yearMonth.value = yearMonth.value.plusMonths(monthsToAdd) }

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        CalendarHeader(yearMonth.value, { changeMonth(-1) }, { changeMonth(1) })
        DayOfWeek(cellWidth)
        CalendarDate(yearMonth = yearMonth.value, disabledDates = disabledDates, cellWidth = cellWidth, isPastDateDisabled = true, rentalStartDate, rentalEndDate, rentalPeriod) { date ->
            if (!isSelectingStartDate && rentalStartDate != null) {
                if (date.isBefore(rentalStartDate)) {
                    if (isSelectingEndDate) {
                        rentalStartDate = null
                        rentalEndDate = date
                        isSelectingEndDate = false
                    } else {
                        rentalStartDate = date
                    }
                } else {
                    rentalEndDate = date
                    isSelectingEndDate = false
                }
            } else {
                if (rentalEndDate != null && date.isAfter(rentalEndDate)) rentalEndDate = null
                rentalStartDate = date
                isSelectingStartDate = false
            }
        }
        Column(modifier = Modifier
            .fillMaxWidth()
            .screenHorizontalPadding(), horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                DateBox(rentalStartDate?.toString() ?: "-", isSelectingStartDate) {
                    isSelectingStartDate = !isSelectingStartDate; isSelectingEndDate = false
                }
                Text(modifier = Modifier.padding(horizontal = 5.dp), text = "부터", style = MaterialTheme.typography.bodyMedium)
                DateBox(rentalEndDate?.toString() ?: "-", isSelectingEndDate) {
                    isSelectingEndDate = !isSelectingEndDate; isSelectingStartDate = false
                }
                Text(modifier = Modifier.padding(horizontal = 5.dp), text = "까지", style = MaterialTheme.typography.bodyMedium)
            }
            totalPeriodText(rentalPeriod)
        }
    }
}
@Composable
fun totalPeriodText(period: Int) {
    Text(
        modifier = Modifier.padding(top = 16.dp),
        text = buildAnnotatedString {
            append("총 ")
            withStyle(style = MaterialTheme.typography.bodyLarge.toSpanStyle().copy(color = PrimaryBlue500)) {
                append("$period 일")
            }
            append(" 대여하고 싶어요")
        },
        style = MaterialTheme.typography.bodyMedium,
        textAlign = TextAlign.Start
    )
}
@Composable
fun DateBox(date: String, isSelectingDate: Boolean, onDateClick: () -> Unit) {
    Box(modifier = Modifier
        .width(120.dp)
        .height(40.dp)
        .basicRoundedGrayBorder(if (isSelectingDate) PrimaryBlue500 else Gray200)
        .clip(RoundedCornerShape(20.dp))
        .clickable { onDateClick() },
        contentAlignment = Alignment.Center) {
        Text(text = date, style = MaterialTheme.typography.bodyMedium)
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RangePickerDateGrid(yearMonth: YearMonth, disabledDates: List<String>, modifier: Modifier, isSelectingStartDate: Boolean, isSelectingEndDate: Boolean) {
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

    var rentalStartDate: LocalDate? by remember { mutableStateOf(null) }
    var rentalEndDate: LocalDate? by remember { mutableStateOf(null) }
    var rentalPeriod: Int = if(rentalStartDate != null && rentalEndDate != null) ChronoUnit.DAYS.between(rentalStartDate, rentalEndDate).toInt() + 1 else 0

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

                val isStartDay = date != null && rentalStartDate?.isEqual(date) == true
                val isEndDay = date != null && rentalEndDate?.isEqual(date) == true
                val isInSelectedPeriod = date != null && rentalPeriod > 0 && date.isAfter(rentalStartDate) && date.isBefore(rentalEndDate)

                Box(modifier = Modifier.padding(vertical = 4.dp)) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(if (isStartDay || isEndDay) 24.dp else 48.dp)
                            .background(if (rentalPeriod > 1 && (isStartDay || isEndDay || isInSelectedPeriod)) PrimaryBlue300 else Color.Transparent)
                            .align(if (isStartDay) Alignment.CenterEnd else if (isEndDay) Alignment.CenterStart else Alignment.Center)
                    )
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .padding(horizontal = 4.dp)
                            .clip(if (isInSelectedPeriod) RectangleShape else CircleShape)
                            .background(
                                if (isDisabled) SecondaryYellow
                                else if (isStartDay || isEndDay) PrimaryBlue500
                                else if (isInSelectedPeriod) PrimaryBlue300
                                else Color.Transparent
                            )
                            .then(
                                if (isToday) Modifier.border(2.dp, PrimaryBlue500, CircleShape)
                                else Modifier
                            )
                            .clickable {
                                if (date != null) {
                                    if (!isSelectingStartDate && rentalStartDate != null && !date.isBefore(
                                            rentalStartDate
                                        )
                                    ) {
                                        rentalEndDate = date
                                    } else {
                                        rentalStartDate = date
                                    }
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = date?.dayOfMonth?.toString() ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (isDisabled || isStartDay || isEndDay) Color.White else AppBlack

                        )
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, apiLevel = 33)
@Composable
fun PreviewDateRangePicker() {
    RentItTheme {
        DateRangePicker(YearMonth.now())
    }
}