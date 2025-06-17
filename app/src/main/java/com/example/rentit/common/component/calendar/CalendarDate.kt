package com.example.rentit.common.component.calendar

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.rentit.common.theme.AppBlack
import com.example.rentit.common.theme.Gray300
import com.example.rentit.common.theme.PrimaryBlue300
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.common.theme.SecondaryYellow
import com.example.rentit.data.product.dto.RequestPeriodDto
import java.time.LocalDate
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarDate(
    yearMonth: YearMonth,
    disabledDates: List<String>,
    cellWidth: Dp,
    isPastDateDisabled: Boolean = false,
    rentalStartDate: LocalDate? = null,
    rentalEndDate: LocalDate? = null,
    rentalPeriod: Int = 0,
    requestPeriodList: List<RequestPeriodDto> = emptyList(),
    onDateClick: (LocalDate) -> Unit = {}
) {
    val daysInMonth = yearMonth.lengthOfMonth()
    val firstDayOfWeek = yearMonth.atDay(1).dayOfWeek.value % 7
    val totalSlots = daysInMonth + firstDayOfWeek
    val weeksInMonth = totalSlots / 7 + if (totalSlots % 7 != 0) 1 else 0
    val isTodayInMonth =
        YearMonth.now().year == yearMonth.year && YearMonth.now().month == yearMonth.month

    val disabledLocalDates = disabledDates.map { LocalDate.parse(it) }
    val disabledDatesInMonth = Array(32) { false }
    disabledLocalDates.forEach {
        disabledDatesInMonth[it.dayOfMonth] =
            yearMonth.year == it.year && yearMonth.month == it.month
    }

    val requestStartDates = mutableListOf<LocalDate>()
    val requestEndDates = mutableListOf<LocalDate>()
    val requestDatesInPeriod = mutableListOf<LocalDate>()

    for (period in requestPeriodList) {
        if (period.startDate.isAfter(period.endDate)) {
            continue
        }
        var currentDate = period.startDate
        while (!currentDate.isAfter(period.endDate)) {
            if(currentDate.isEqual(period.startDate)){
                requestStartDates.add(currentDate)
            } else if(currentDate.isEqual(period.endDate)){
                requestEndDates.add(currentDate)
            } else {
                requestDatesInPeriod.add(currentDate)
            }
            currentDate = currentDate.plusDays(1) // 다음 날짜로 이동
        }
    }

    Column(
        modifier = Modifier
            .padding(bottom = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        repeat(weeksInMonth) {
            Row(
                modifier = Modifier
                    .height(cellWidth)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                for (i in 0..6) {
                    val index = it * 7 + i
                    val dayNumber = index - firstDayOfWeek + 1
                    val date = if (dayNumber in 1..daysInMonth) yearMonth
                        .atDay(dayNumber) else null

                    var isToday = false
                    var isDisabled = false
                    var isPastDate = false

                    // 드래그 날짜 선택
                    var isStartDay = false
                    var isEndDay = false
                    var isInSelectedPeriod = false

                    // 요청 기간 표시
                    var isRequestStartDay = false
                    var isRequestEndDay = false
                    var isRequestDateInPeriod = false

                    date?.let {
                        isToday = isTodayInMonth && LocalDate.now().dayOfMonth == date.dayOfMonth
                        isDisabled = disabledDatesInMonth[dayNumber]
                        isPastDate =
                            isPastDateDisabled && date.isBefore(LocalDate.now()) // 오늘 이전의 날짜 (과거 날짜 비활성화시)

                        isStartDay = rentalStartDate?.isEqual(date) == true
                        isEndDay = rentalEndDate?.isEqual(date) == true
                        isInSelectedPeriod = rentalPeriod > 0 && date.isAfter(rentalStartDate) && date.isBefore(
                                rentalEndDate
                            )

                        isRequestStartDay = date in requestStartDates
                        isRequestEndDay = date in requestEndDates
                        isRequestDateInPeriod = date in requestDatesInPeriod
                    }

                    Box(modifier = Modifier.padding(vertical = 4.dp)) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(if (isStartDay || isEndDay || isRequestStartDay || isRequestEndDay) cellWidth.div(2) else cellWidth)
                                .background(if ((rentalPeriod > 1 && (isStartDay || isEndDay || isInSelectedPeriod)) || isRequestStartDay || isRequestEndDay || isRequestDateInPeriod) PrimaryBlue300 else Color.Transparent)
                                .align(if (isStartDay || isRequestStartDay) Alignment.CenterEnd else if (isEndDay || isRequestEndDay) Alignment.CenterStart else Alignment.Center)
                        )
                        Box(
                            modifier = Modifier
                                .size(cellWidth)
                                .padding(horizontal = 4.dp)
                                .clip(if (isInSelectedPeriod || isRequestDateInPeriod) RectangleShape else CircleShape)
                                .background(
                                    if (isDisabled && !isPastDate) SecondaryYellow
                                    else if (isStartDay || isEndDay) PrimaryBlue500
                                    else if (isInSelectedPeriod || isRequestStartDay || isRequestEndDay || isRequestDateInPeriod) PrimaryBlue300
                                    else Color.Transparent
                                )
                                .then(
                                    if (isToday) Modifier.border(2.dp, PrimaryBlue500, CircleShape)
                                    else Modifier
                                )
                                .clickable(enabled = date != null && !isDisabled && !isPastDate) {
                                    if (date != null) onDateClick(date)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = date?.dayOfMonth?.toString() ?: "",
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (isPastDate) Gray300
                                else if (isDisabled || isStartDay || isEndDay) Color.White
                                else AppBlack

                            )
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, apiLevel = 33)
@Composable
fun PreviewCalendarDate() {
    RentItTheme {
        CalendarDate(
            YearMonth.now(), emptyList(),
            48.dp,
        )
    }
}