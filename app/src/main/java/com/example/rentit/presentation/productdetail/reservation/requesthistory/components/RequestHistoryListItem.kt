package com.example.rentit.presentation.productdetail.reservation.requesthistory.components

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentit.R
import com.example.rentit.common.util.getKorLabel
import com.example.rentit.common.theme.AppBlack
import com.example.rentit.common.theme.Gray300
import com.example.rentit.common.theme.Gray400
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.common.util.formatRentalPeriod
import com.example.rentit.common.util.parseLocalDateOrNull
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
    val startDate = parseLocalDateOrNull(requestInfo.startDate)
    val isPast = startDate?.isBefore(LocalDate.now()) ?: true
    val pastDateColor = Gray300

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
                    style = MaterialTheme.typography.labelMedium,
                    color = if (isPast) pastDateColor else AppBlack
                )
                Text(
                    text = "${requestedAt.format(formatter)} ${requestedAt.dayOfWeek.getKorLabel()}",
                    style = MaterialTheme.typography.labelMedium,
                    color = if (isPast) pastDateColor else Gray400
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formatRentalPeriod(LocalContext.current, requestInfo.startDate, requestInfo.endDate),
                    style = MaterialTheme.typography.labelLarge,
                    color = if (isPast) pastDateColor else AppBlack
                )
                TextButton(
                    modifier = Modifier.padding(start = 20.dp),
                    onClick = onStartChatClick,
                    enabled = !isPast
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.padding(end = 4.dp),
                            text = stringResource(id = R.string.request_history_list_item_text_chat),
                            style = MaterialTheme.typography.labelLarge,
                            color = if (isPast) pastDateColor else PrimaryBlue500
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.ic_chevron_right),
                            contentDescription = stringResource(
                                id = R.string.content_description_for_ic_chevron_right
                            ),
                            tint = if (isPast) pastDateColor else PrimaryBlue500
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
private fun Preview() {
    RentItTheme {
        //RequestHistoryListItem() {}
    }
}
