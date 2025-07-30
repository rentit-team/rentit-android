package com.example.rentit.presentation.rentaldetail.common.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.rentit.R
import com.example.rentit.common.theme.Gray800
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.common.util.daysBetween
import com.example.rentit.common.util.formatPrice
import com.example.rentit.common.util.formatRentalPeriod

/**
* 대여 정보(상품 사진, 게시글 제목, 대여 기간, 총 금액)을 함께 표시하는 공통 UI 컴포넌트
 */

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RentalSummary(
    modifier: Modifier = Modifier,
    thumbnailImgUrl: String? = null,
    productTitle: String,
    startDate: String,
    endDate: String,
    pricePerDay: Int,
    depositBasisDays: Int = 0,
) {
    val period = daysBetween(startDate, endDate)
    val totalPrice = formatPrice(pricePerDay * period + pricePerDay * depositBasisDays)

    Row(modifier.fillMaxWidth(), verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
        AsyncImage(
            modifier = Modifier
                .size(62.dp)
                .clip(RoundedCornerShape(20.dp)),
            model = ImageRequest.Builder(LocalContext.current)
                .data(thumbnailImgUrl)
                .error(R.drawable.img_thumbnail_placeholder)
                .placeholder(R.drawable.img_thumbnail_placeholder)
                .fallback(R.drawable.img_thumbnail_placeholder)
                .build(),
            contentDescription = stringResource(id = R.string.common_list_item_thumbnail_img_placeholder_description),
            contentScale = ContentScale.Crop
        )
        Column(Modifier.padding(start = 15.dp)) {
            Text(text = productTitle, style = MaterialTheme.typography.labelMedium)
            Text(
                modifier = Modifier.padding(vertical = 6.dp),
                text = formatRentalPeriod(LocalContext.current, startDate, endDate),
                style = MaterialTheme.typography.labelMedium,
                color = Gray800
            )
            Text(
                text = "${stringResource(R.string.common_total)} $totalPrice ${stringResource(R.string.common_price_unit)}",
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(showBackground = true)
private fun Preview() {
    RentItTheme {
        RentalSummary(
            productTitle = "캐논 EOS 550D",
            startDate = "2025-08-17",
            endDate = "2025-08-20",
            pricePerDay = 10000,
            depositBasisDays = 3
        )
    }
}