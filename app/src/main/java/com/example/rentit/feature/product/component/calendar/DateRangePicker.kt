package com.example.rentit.feature.product.component.calendar

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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.rentit.common.component.basicRoundedGrayBorder
import com.example.rentit.common.component.screenHorizontalPadding
import com.example.rentit.common.theme.Gray200
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.feature.product.ProductViewModel
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateRangePicker(productViewModel: ProductViewModel, yearMonth: YearMonth = YearMonth.now(), disabledDates: List<String> = listOf("2025-04-03", "2025-04-28", "2025-05-01", "2025-05-03"), modifier: Modifier = Modifier) {
    val yearMonth = remember { mutableStateOf(yearMonth) }
    val cellWidth = 48.dp

    // 시작일 설정, 종료일 설정 모드 확인
    var isSelectingStartDate by remember { mutableStateOf(false) }
    var isSelectingEndDate by remember { mutableStateOf(false) }

    val rentalStartDate = productViewModel.bookingStartDate.collectAsStateWithLifecycle()
    val rentalEndDate = productViewModel.bookingEndDate.collectAsStateWithLifecycle()
    val rentalPeriod = productViewModel.bookingPeriod.collectAsStateWithLifecycle()

    fun changeMonth(monthsToAdd: Long) { yearMonth.value = yearMonth.value.plusMonths(monthsToAdd) }

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        CalendarHeader(yearMonth.value, { changeMonth(-1) }, { changeMonth(1) })
        DayOfWeek(cellWidth)
        CalendarDate(yearMonth = yearMonth.value, disabledDates = disabledDates, cellWidth = cellWidth, isPastDateDisabled = true, rentalStartDate.value, rentalEndDate.value, rentalPeriod.value) { date ->
            if (!isSelectingStartDate && rentalStartDate.value != null) {
                if (date.isBefore(rentalStartDate.value)) {
                    if (isSelectingEndDate) {
                        productViewModel.setBookingStartDate(null)
                        productViewModel.setBookingEndDate(date)
                        isSelectingEndDate = false
                    } else {
                        productViewModel.setBookingStartDate(date)
                    }
                } else {
                    productViewModel.setBookingEndDate(date)
                    isSelectingEndDate = false
                }
            } else {
                if (rentalEndDate.value != null && date.isAfter(rentalEndDate.value)) productViewModel.setBookingEndDate(null)
                productViewModel.setBookingStartDate(date)
                isSelectingStartDate = false
            }
        }
        Column(modifier = Modifier
            .fillMaxWidth()
            .screenHorizontalPadding(), horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                DateBox(rentalStartDate.value?.toString() ?: "-", isSelectingStartDate) {
                    isSelectingStartDate = !isSelectingStartDate; isSelectingEndDate = false
                }
                Text(modifier = Modifier.padding(horizontal = 5.dp), text = "부터", style = MaterialTheme.typography.bodyMedium)
                DateBox(rentalEndDate.value?.toString() ?: "-", isSelectingEndDate) {
                    isSelectingEndDate = !isSelectingEndDate; isSelectingStartDate = false
                }
                Text(modifier = Modifier.padding(horizontal = 5.dp), text = "까지", style = MaterialTheme.typography.bodyMedium)
            }
            totalPeriodText(rentalPeriod.value)
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
@Preview(showBackground = true, apiLevel = 33)
@Composable
fun PreviewDateRangePicker() {
    RentItTheme {
        DateRangePicker(hiltViewModel(), YearMonth.now())
    }
}