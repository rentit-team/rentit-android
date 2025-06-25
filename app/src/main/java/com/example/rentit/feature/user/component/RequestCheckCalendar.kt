package com.example.rentit.feature.user.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentit.common.component.calendar.CalendarDate
import com.example.rentit.common.component.calendar.CalendarHeader
import com.example.rentit.common.component.calendar.DayOfWeek
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.data.product.dto.RequestPeriodDto
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RequestCheckCalendar(requestPeriodList: List<RequestPeriodDto>, onChangeMonth: (month: YearMonth) -> Unit = {}) {
    var yearMonth by remember { mutableStateOf(YearMonth.now()) }
    val cellWidth = 48.dp

    fun changeMonth(monthsToAdd: Long) {
        yearMonth = yearMonth.plusMonths(monthsToAdd)
        onChangeMonth(yearMonth)
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        CalendarHeader(yearMonth, { changeMonth(-1) }, { changeMonth(1) })
        DayOfWeek(cellWidth)
        CalendarDate(
            yearMonth = yearMonth,
            disabledDates = emptyList(),
            cellWidth = cellWidth,
            isPastDateDisabled = true,
            requestPeriodList = requestPeriodList
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewRequestCheckCalendar() {
    RentItTheme {
        RequestCheckCalendar(emptyList())
    }
}