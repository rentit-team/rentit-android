package com.example.rentit.presentation.productdetail.rentalhistory.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentit.common.component.calendar.CalendarDate
import com.example.rentit.common.component.calendar.CalendarHeader
import com.example.rentit.common.component.calendar.DayOfWeek
import com.example.rentit.common.theme.RentItTheme
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RentalHistoryCalendar(yearMonth: YearMonth, disabledDates: List<String> = listOf("2025-04-03", "2025-04-28", "2025-05-01", "2025-05-03"), modifier: Modifier = Modifier) {
    var yearMonth = remember { mutableStateOf(yearMonth) }
    val cellWidth = 48.dp

    fun changeMonth(monthsToAdd: Long) { yearMonth.value = yearMonth.value.plusMonths(monthsToAdd) }

    Column(modifier = modifier.padding(bottom = 32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        CalendarHeader(yearMonth.value, { changeMonth(-1) }, { changeMonth(1) })
        DayOfWeek(cellWidth)
        CalendarDate(yearMonth = yearMonth.value, disabledDates = disabledDates, cellWidth = cellWidth)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
private fun Preview() {
    RentItTheme {
        RentalHistoryCalendar(YearMonth.now())
    }
}