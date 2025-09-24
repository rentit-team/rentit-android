package com.example.rentit.presentation.productdetail.reservation.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.rentit.common.component.rentItBasicRoundedGrayBorder
import com.example.rentit.common.component.calendar.CalendarDate
import com.example.rentit.common.component.calendar.CalendarHeader
import com.example.rentit.common.component.calendar.DayOfWeek
import com.example.rentit.common.component.rentItScreenHorizontalPadding
import com.example.rentit.common.theme.Gray200
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.theme.RentItTheme
import java.time.LocalDate
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateRangePicker(
    modifier: Modifier = Modifier,
    rentalStartDate: LocalDate?,
    rentalEndDate: LocalDate?,
    selectedPeriod: Int,
    disabledDates: List<String> = emptyList(),
    setRentalStartDate: (LocalDate?) -> Unit,
    setRentalEndDate: (LocalDate?) -> Unit,
) {
    val yearMonth = remember { mutableStateOf(YearMonth.now()) }
    val cellWidth = 48.dp

    // 시작일 설정, 종료일 설정 모드 확인
    var isSelectingStartDate by remember { mutableStateOf(false) }
    var isSelectingEndDate by remember { mutableStateOf(false) }

    fun changeMonth(monthsToAdd: Long) { yearMonth.value = yearMonth.value.plusMonths(monthsToAdd) }

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        CalendarHeader(yearMonth.value, { changeMonth(-1) }, { changeMonth(1) })
        DayOfWeek(cellWidth)
        CalendarDate(yearMonth = yearMonth.value, disabledDates = disabledDates, cellWidth = cellWidth, isPastDateDisabled = true, rentalStartDate, rentalEndDate, selectedPeriod) { date ->
            if (!isSelectingStartDate && rentalStartDate != null) {
                if (date.isBefore(rentalStartDate)) {
                    if (isSelectingEndDate) {
                        setRentalStartDate(null)
                        setRentalEndDate(date)
                        isSelectingEndDate = false
                    } else {
                        setRentalStartDate(date)
                    }
                } else {
                    setRentalEndDate(date)
                    isSelectingEndDate = false
                }
            } else {
                if (rentalEndDate != null && date.isAfter(rentalEndDate)) setRentalEndDate(null)
                setRentalStartDate(date)
                isSelectingStartDate = false
            }
        }
        Column(modifier = Modifier
            .padding(top = 20.dp)
            .fillMaxWidth()
            .rentItScreenHorizontalPadding(), horizontalAlignment = Alignment.CenterHorizontally) {
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
            TotalPeriodText(selectedPeriod)
        }
    }
}
@Composable
fun TotalPeriodText(period: Int) {
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
        .width(110.dp)
        .height(40.dp)
        .rentItBasicRoundedGrayBorder(if (isSelectingDate) PrimaryBlue500 else Gray200)
        .clip(RoundedCornerShape(20.dp))
        .clickable { onDateClick() },
        contentAlignment = Alignment.Center) {
        Text(text = date, style = MaterialTheme.typography.bodyMedium)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, apiLevel = 33)
@Composable
private fun Preview() {
    RentItTheme {
        DateRangePicker(
            hiltViewModel(),
            rentalStartDate = null,
            rentalEndDate = null,
            selectedPeriod = 0,
            setRentalStartDate = { },
            setRentalEndDate = { },
        )
    }
}