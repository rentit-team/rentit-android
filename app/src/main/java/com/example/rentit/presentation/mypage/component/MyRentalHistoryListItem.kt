package com.example.rentit.presentation.mypage.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.rentit.R
import com.example.rentit.common.component.getKorDayOfWeek
import com.example.rentit.common.enums.ResvStatus
import com.example.rentit.common.theme.Gray400
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.common.theme.SecondaryYellow
import com.example.rentit.data.product.dto.OwnerDto
import com.example.rentit.data.product.dto.PeriodDto
import com.example.rentit.data.product.dto.ProductDto
import com.example.rentit.data.user.dto.ReservationDto
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyRentalHistoryListItem(rentalInfo: ReservationDto) {
    val formatter = DateTimeFormatter.ofPattern("yy.MM.dd")
    val requestedAt = LocalDateTime.parse(rentalInfo.requestedAt).toLocalDate()
    val startDate = LocalDate.parse(rentalInfo.startDate)
    val endDate = LocalDate.parse(rentalInfo.endDate)
    val period = ChronoUnit.DAYS.between(startDate, endDate).toInt() + 1

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(25.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier
                .size(70.dp)
                .clip(RoundedCornerShape(20.dp)),
            model = ImageRequest.Builder(LocalContext.current)
                .data(rentalInfo.product.thumbnailImgUrl)
                .error(R.drawable.img_thumbnail_placeholder)
                .placeholder(R.drawable.img_thumbnail_placeholder)
                .fallback(R.drawable.img_thumbnail_placeholder)
                .build(),
            contentDescription = stringResource(id = R.string.common_list_item_thumbnail_img_placeholder_description),
            contentScale = ContentScale.Crop
        )
        Column(Modifier.padding(start = 18.dp)) {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    modifier = Modifier.padding(end = 8.dp),
                    text = rentalInfo.product.title,
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    text = if(rentalInfo.status == ResvStatus.ACCEPTED.name)
                        stringResource(R.string.screen_mypage_my_rental_list_item_label_renting)
                    else ResvStatus.fromLabel(rentalInfo.status)?.label ?: "",
                    style = MaterialTheme.typography.labelLarge,
                    color = if(rentalInfo.status == ResvStatus.ACCEPTED.name) SecondaryYellow else Gray400
                )
            }
            Text(
                modifier = Modifier.padding(top = 5.dp, bottom = 8.dp),
                text = stringResource(
                    id = R.string.request_history_list_item_period,
                    startDate.format(formatter),
                    getKorDayOfWeek(startDate.dayOfWeek.toString()),
                    endDate.format(formatter),
                    getKorDayOfWeek(endDate.dayOfWeek.toString()),
                    period
                ),
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = "${stringResource(R.string.screen_mypage_my_rental_list_item_label_request_at)} ${requestedAt.format(formatter)}",
                style = MaterialTheme.typography.labelMedium,
                color = Gray400
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun PreviewRentalHistoryListItem() {
    val sampleReservation = ReservationDto(
        reservationId = 1001,
        product = ProductDto(
            productId = 1,
            title = "캐논 EOS 550D",
            description = "가볍고 튼튼해요",
            price = 10000,
            thumbnailImgUrl = "",
            region = "서울특별시",
            period = PeriodDto(
                cycle = "daily",
                min = 1,
                max = 6
            ),
            owner = OwnerDto(
                userId = 1,
                nickname = "예준"
            ),
            status = "AVAILABLE",
            categories = emptyList(),
            createdAt = "2025-03-22T12:00:00"
        ),
        startDate = "2025-04-15",
        endDate = "2025-04-17",
        status = "PENDING",
        requestedAt = "2025-03-20T14:00:00"
    )
    RentItTheme {
        MyRentalHistoryListItem(sampleReservation)
    }
}
