package com.example.rentit.feature.product

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.rentit.common.component.CommonCalendar
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.feature.product.component.UsageListItem
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UsageDetailBottomDrawer() {
    Column(modifier = Modifier.fillMaxHeight(0.85f)) {
        CommonCalendar(yearMonth = YearMonth.now())
        UsageListItem()
        UsageListItem()
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewUsageDetailBottomDrawer() {
    RentItTheme {
        UsageDetailBottomDrawer()
    }
}