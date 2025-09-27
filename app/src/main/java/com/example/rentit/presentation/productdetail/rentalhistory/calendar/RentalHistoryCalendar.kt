package com.example.rentit.presentation.productdetail.rentalhistory.calendar

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentit.common.ui.component.calendar.CalendarHeader
import com.example.rentit.common.ui.component.calendar.DayOfWeek
import com.example.rentit.common.enums.RentalStatus
import com.example.rentit.common.theme.AppBlack
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.presentation.productdetail.rentalhistory.model.RentalHistoryDateModel
import java.time.LocalDate
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RentalHistoryCalendar(
    rentalHistoryByDateMap: Map<LocalDate, RentalHistoryDateModel>,
    calendarMonth: YearMonth,
    onChangeMonth: (Long) -> Unit = {},
    onRentalDateClick: (Int) -> Unit = {}
) {
    val cellWidth = 48.dp

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        CalendarHeader(calendarMonth, { onChangeMonth(-1) }, { onChangeMonth(1) })
        DayOfWeek(cellWidth)
        RentalHistoryCalendarDates(
            yearMonth = calendarMonth,
            rentalHistoryByDateMap = rentalHistoryByDateMap,
            onRentalDateClick = onRentalDateClick
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RentalHistoryCalendarDates(
    yearMonth: YearMonth,
    rentalHistoryByDateMap: Map<LocalDate, RentalHistoryDateModel> = emptyMap(),
    onRentalDateClick: (Int) -> Unit = {}
) {
    val daysInMonth = yearMonth.lengthOfMonth()
    val firstDayOfWeek = yearMonth.atDay(1).dayOfWeek.value % 7
    val totalSlots = daysInMonth + firstDayOfWeek
    val weeksInMonth = totalSlots / 7 + if (totalSlots % 7 != 0) 1 else 0
    val isTodayInMonth = YearMonth.now().year == yearMonth.year && YearMonth.now().month == yearMonth.month

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        repeat(weeksInMonth) {
            Row(
                modifier = Modifier
                    .height(48.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                (0..6).forEach { i ->
                    val index = it * 7 + i
                    val dayNumber = index - firstDayOfWeek + 1
                    val date = if (dayNumber in 1..daysInMonth) yearMonth.atDay(dayNumber) else null

                    val isToday = date?.let { isTodayInMonth && LocalDate.now().dayOfMonth == date.dayOfMonth } ?: false
                    val rentalDate = date?.let { rentalHistoryByDateMap[date] }

                    val markerModifier = Modifier.background(
                        color = rentalDate?.rentalStatus?.color ?: Color.Transparent,
                        shape = RoundedCornerShape(20.dp)
                    )

                    Box(modifier = Modifier
                        .size(48.dp)
                        .padding(4.dp)) {
                        Box(
                            modifier = markerModifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .clickable { rentalDate?.let { onRentalDateClick(rentalDate.reservationId) } }
                        )

                        if (isToday) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .border(2.dp, PrimaryBlue500, CircleShape)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Transparent),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = date?.dayOfMonth?.toString() ?: "",
                                style = MaterialTheme.typography.bodyMedium,
                                color = rentalDate?.let { Color.White } ?: AppBlack
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
fun RentalHistoryCalendarPreview() {
    val sampleRentalHistoryByDateMap: Map<LocalDate, RentalHistoryDateModel> = mapOf(
        LocalDate.of(2025, 9, 12) to RentalHistoryDateModel(
            reservationId = 101,
            rentalStatus = RentalStatus.RENTING
        ),
        LocalDate.of(2025, 9, 13) to RentalHistoryDateModel(
            reservationId = 102,
            rentalStatus = RentalStatus.PAID
        ),
        LocalDate.of(2025, 9, 14) to RentalHistoryDateModel(
            reservationId = 103,
            rentalStatus = RentalStatus.CANCELED
        )
    )

    RentItTheme {
        RentalHistoryCalendar(
            sampleRentalHistoryByDateMap,
            YearMonth.now()
        )
    }
}