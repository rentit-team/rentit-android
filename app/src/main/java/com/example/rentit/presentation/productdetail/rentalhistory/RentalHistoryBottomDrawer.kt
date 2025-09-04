package com.example.rentit.presentation.productdetail.rentalhistory

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.rentit.presentation.productdetail.rentalhistory.components.RentalHistoryCalendar
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.presentation.productdetail.rentalhistory.components.CalendarLegend
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RentalHistoryBottomDrawer(productId: Int) {
    val rentalHistoryViewModel: RentalHistoryViewModel = hiltViewModel()
    val reservedDateList by rentalHistoryViewModel.reservedDateList.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        rentalHistoryViewModel.getReservedDates(productId)
    }

    Column(
        modifier = Modifier.fillMaxHeight(0.7f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CalendarLegend()
        RentalHistoryCalendar(yearMonth = YearMonth.now(), reservedDateList)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
private fun Preview() {
    RentItTheme {
        RentalHistoryBottomDrawer(0)
    }
}