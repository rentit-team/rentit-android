package com.example.rentit.feature.productdetail.rentalhistory

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.rentit.feature.productdetail.rentalhistory.components.ReadOnlyCalender
import com.example.rentit.common.theme.RentItTheme
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UsageDetailBottomDrawer(reservedDateList: List<String>) {
    Column(modifier = Modifier.fillMaxHeight(0.85f)) {
        ReadOnlyCalender(yearMonth = YearMonth.now(), reservedDateList)
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
fun PreviewUsageDetailBottomDrawer() {
    RentItTheme {
        UsageDetailBottomDrawer(emptyList())
    }
}