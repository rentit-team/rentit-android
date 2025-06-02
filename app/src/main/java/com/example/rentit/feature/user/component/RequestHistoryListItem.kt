package com.example.rentit.feature.user.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentit.R
import com.example.rentit.common.component.basicListItemTopDivider
import com.example.rentit.common.theme.Gray200
import com.example.rentit.common.theme.Gray400
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.data.user.dto.RequestInfoDto
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RequestHistoryListItem(requestInfo: RequestInfoDto) {

    val startDate = LocalDate.parse(requestInfo.startDate)
    val endDate = LocalDate.parse(requestInfo.endDate)
    val period = ChronoUnit.DAYS.between(startDate, endDate).toInt() + 1

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 23.dp, horizontal = 25.dp)
            .basicListItemTopDivider()
    ) {
        Row(modifier = Modifier.padding(bottom = 10.dp), verticalAlignment = Alignment.Bottom) {
            Text(
                modifier = Modifier.padding(end = 8.dp),
                text = requestInfo.renterNickName,
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = requestInfo.requestedAt,
                style = MaterialTheme.typography.labelMedium,
                color = Gray400
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(
                    id = R.string.request_history_list_item_period,
                    requestInfo.startDate,
                    startDate.dayOfWeek,
                    requestInfo.endDate,
                    endDate.dayOfWeek,
                    period
                ),
                style = MaterialTheme.typography.bodyMedium
            )
            Row(
                modifier = Modifier.padding(start = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(end = 4.dp),
                    text = stringResource(id = R.string.request_history_list_item_text_chat),
                    style = MaterialTheme.typography.labelLarge,
                    color = PrimaryBlue500
                )
                Icon(
                    modifier = Modifier.size(10.dp),
                    painter = painterResource(id = R.drawable.ic_chevron_right),
                    contentDescription = stringResource(
                        id = R.string.content_description_for_ic_chevron_right
                    ),
                    tint = PrimaryBlue500
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRequestHistoryListItem() {
    RentItTheme {
        // RequestHistoryListItem()
    }
}
