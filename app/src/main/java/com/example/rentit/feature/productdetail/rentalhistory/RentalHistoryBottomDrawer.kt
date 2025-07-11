package com.example.rentit.feature.productdetail.rentalhistory

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.rentit.feature.productdetail.rentalhistory.components.RentalHistoryCalendar
import com.example.rentit.common.theme.RentItTheme
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RentalHistoryBottomDrawer(productId: Int) {
    val rentalHistoryViewModel: RentalHistoryViewModel = hiltViewModel()
    val reservedDateList by rentalHistoryViewModel.reservedDateList.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        rentalHistoryViewModel.getReservedDates(productId)
    }

    Column(modifier = Modifier.fillMaxHeight(0.85f)) {
        RentalHistoryCalendar(yearMonth = YearMonth.now(), reservedDateList)

        // 대여 이력 리스트 표시 여부 결정 후 추가 또는 삭제 예정
        /*LazyColumn(modifier = Modifier.padding(bottom = 25.dp)) {
            this.items(reservedDateList) {
                UsageListItem()
            }
        }*/
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