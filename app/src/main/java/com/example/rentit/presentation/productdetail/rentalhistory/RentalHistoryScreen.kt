package com.example.rentit.presentation.productdetail.rentalhistory

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentit.common.component.CommonTopAppBar
import com.example.rentit.common.component.calendar.CalendarDate
import com.example.rentit.common.component.calendar.CalendarHeader
import com.example.rentit.common.component.calendar.DayOfWeek
import com.example.rentit.common.component.screenHorizontalPadding
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.data.product.dto.RequestPeriodDto
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RentalHistoryScreen(
    requestPeriodList: List<RequestPeriodDto>,
    onChangeMonth: (YearMonth) -> Unit = {},
    onBackClick: () -> Unit,
) {
    Scaffold(
        topBar = { CommonTopAppBar(title = "대여 내역", onBackClick = onBackClick) }
    ) {
        Column(
            modifier = Modifier.padding(it)
        ) {
            RentalHistoryCalendar(requestPeriodList, onChangeMonth)
            LazyColumn(
                modifier = Modifier.screenHorizontalPadding(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) { }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RentalHistoryCalendar(requestPeriodList: List<RequestPeriodDto>, onChangeMonth: (YearMonth) -> Unit = {}) {
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
private fun RentalHistoryScreenPreview() {
    RentItTheme {
        RentalHistoryScreen(
            requestPeriodList = emptyList(),
            onBackClick = {}
        )
    }
}
