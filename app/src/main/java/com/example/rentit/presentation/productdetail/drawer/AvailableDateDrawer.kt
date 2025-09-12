package com.example.rentit.presentation.productdetail.drawer

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentit.R
import com.example.rentit.common.component.calendar.CalendarDate
import com.example.rentit.common.component.calendar.CalendarHeader
import com.example.rentit.common.component.calendar.DayOfWeek
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.common.theme.SecondaryYellow
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AvailableDateDrawer(reservedDateList: List<String>) {
    Column(
        modifier = Modifier.fillMaxHeight(0.7f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CalendarLegend()
        AvailableDateCalendar(yearMonth = YearMonth.now(), reservedDateList)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarLegend() {
    Row(
        modifier = Modifier.padding(bottom = 15.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(SecondaryYellow),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "0",
                style = MaterialTheme.typography.labelMedium,
                color = Color.White
            )
        }

        Text(
            text = stringResource(R.string.drawer_rental_history_calendar_legend_text),
            style = MaterialTheme.typography.labelMedium,
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AvailableDateCalendar(yearMonth: YearMonth, disabledDates: List<String> = emptyList()) {
    val month = remember { mutableStateOf(yearMonth) }
    val cellWidth = 48.dp

    fun changeMonth(monthsToAdd: Long) { month.value = month.value.plusMonths(monthsToAdd) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        CalendarHeader(month.value, { changeMonth(-1) }, { changeMonth(1) })
        DayOfWeek(cellWidth)
        CalendarDate(yearMonth = month.value, disabledDates = disabledDates, cellWidth = cellWidth)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
private fun Preview() {
    RentItTheme {
        AvailableDateDrawer(emptyList())
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
private fun CalendarLegendPreview() {
    RentItTheme {
        CalendarLegend()
    }
}