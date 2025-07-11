package com.example.rentit.feature.productdetail.rentalhistory

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.rentit.feature.productdetail.rentalhistory.components.RentalHistoryCalendar
import com.example.rentit.common.theme.RentItTheme
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RentalHistoryBottomDrawer(reservedDateList: List<String>) {
    Column(modifier = Modifier.fillMaxHeight(0.85f)) {
        RentalHistoryCalendar(yearMonth = YearMonth.now(), reservedDateList)
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
        RentalHistoryBottomDrawer(emptyList())
    }
}