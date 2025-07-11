package com.example.rentit.feature.productdetail.rentalhistory.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentit.R
import com.example.rentit.common.component.screenHorizontalPadding
import com.example.rentit.common.theme.Gray200
import com.example.rentit.common.theme.Gray400
import com.example.rentit.common.theme.RentItTheme
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UsageListItem(
    periodCnt: Int = 0,
    startDate: LocalDate = LocalDate.now(),
    endDate: LocalDate = LocalDate.now(),
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                drawLine(
                    color = Gray200,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = 1.dp.toPx()
                )
            })
    {
        Column(
            modifier = Modifier
                .screenHorizontalPadding()
                .padding(vertical = 26.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.usage_list_item_title_rent_booking),
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    text = stringResource(R.string.usage_list_item_text_rent_period_count, periodCnt),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Text(
                modifier = Modifier.padding(top = 6.dp),
                text = stringResource(R.string.usage_list_item_text_rent_period, startDate.toString(), endDate.toString()),
                style = MaterialTheme.typography.labelMedium,
                color = Gray400
            )
        }
    }

}
@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewUsageListItem() {
    RentItTheme {
        UsageListItem()
    }
}
