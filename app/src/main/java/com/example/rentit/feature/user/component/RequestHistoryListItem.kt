package com.example.rentit.feature.user.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentit.R
import com.example.rentit.common.component.getKorDayOfWeek
import com.example.rentit.common.theme.Gray400
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.data.product.dto.RequestInfoDto
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RequestHistoryListItem(requestInfo: RequestInfoDto, onStartChatClick: () -> Unit) {

    val formatter = DateTimeFormatter.ofPattern("yy.MM.dd")

    val requestedAt = LocalDateTime.parse(requestInfo.requestedAt).toLocalDate()
    val startDate = LocalDate.parse(requestInfo.startDate)
    val endDate = LocalDate.parse(requestInfo.endDate)
    val period = ChronoUnit.DAYS.between(startDate, endDate).toInt() + 1

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        shape = RoundedCornerShape(25.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(start = 30.dp, end = 18.dp, top = 26.dp, bottom = 10.dp)
        ) {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    modifier = Modifier.padding(end = 8.dp),
                    text = requestInfo.renterNickName,
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = "${requestedAt.format(formatter)} ${getKorDayOfWeek(requestedAt.dayOfWeek.toString())}",
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
                        startDate.format(formatter),
                        getKorDayOfWeek(startDate.dayOfWeek.toString()),
                        endDate.format(formatter),
                        getKorDayOfWeek(endDate.dayOfWeek.toString()),
                        period
                    ),
                    style = MaterialTheme.typography.labelLarge
                )
                TextButton(modifier = Modifier.padding(start = 20.dp), onClick = onStartChatClick) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.padding(end = 4.dp),
                            text = stringResource(id = R.string.request_history_list_item_text_chat),
                            style = MaterialTheme.typography.labelLarge,
                            color = PrimaryBlue500
                        )
                        Icon(
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
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun PreviewRequestHistoryListItem() {
    RentItTheme {
        //RequestHistoryListItem() {}
    }
}
